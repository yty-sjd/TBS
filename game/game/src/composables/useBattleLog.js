import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { startGame, doTurn, startAndPlay, getLog, getRolesByIds } from '../api/game'
import { parseLine, applyEvent, cloneState } from '../utils/logParser'

export function useBattleLog() {
  const sessionId = ref('')
  const roleInput = ref('')
  const loading = ref(false)
  const currentStep = ref(0)
  const autoPlaying = ref(false)
  const showLog = ref(false)
  const steps = ref([])
  const currentAction = ref(null)
  const gameOver = ref(false)
  const logContainer = ref(null)
  let autoPlayTimer = null

  const visibleSteps = computed(() => {
    const total = steps.value.length
    const cur = currentStep.value
    const start = Math.max(0, cur - 20)
    const end = Math.min(total, cur + 30)
    return steps.value.slice(start, end)
  })

  const currentState = computed(() => {
    if (steps.value.length === 0) return null
    const step = steps.value[currentStep.value]
    return step ? step.state : null
  })

  const currentEvent = computed(() => {
    if (steps.value.length === 0) return null
    const step = steps.value[currentStep.value]
    return step ? parseLine(step.text) : null
  })

  function getEffect(team, pos) {
    const evt = currentEvent.value
    if (!evt) return { type: '', value: 0 }
    if (evt.type === 'attack') {
      if (evt.team === team && evt.pos === pos) return { type: 'attack', value: evt.damage }
      if (evt.targetTeam === team && evt.targetPos === pos) return { type: 'damage', value: evt.damage }
    }
    if (evt.type === 'damage' && evt.team === team && evt.pos === pos) return { type: 'damage', value: evt.amount }
    if (evt.type === 'death' && evt.team === team && evt.pos === pos) return { type: 'death', value: 0 }
    if (evt.type === 'hp' && evt.team === team && evt.pos === pos) return { type: 'heal', value: evt.value }
    return { type: '', value: 0 }
  }

  /**
   * 从数据库角色数据 + 输入顺序构建初始状态
   * roleInput: "1 2 3 4 5 6 7 8 9 10"
   * 前5个 → 0阵营(0~4号位)，后5个 → 1阵营(0~4号位)
   */
  function initStateFromInput(rolesData, roleInput) {
    const inputIds = roleInput.trim().split(/\s+/).map(Number)
    const roleMap = {}
    for (const r of rolesData) {
      roleMap[r.roleId] = r
    }
    const teams = [[], []]
    for (let i = 0; i < inputIds.length && i < 10; i++) {
      const id = inputIds[i]
      const r = roleMap[id]
      if (!r) continue
      const teamIdx = i < 5 ? 0 : 1
      const pos = i < 5 ? i : i - 5
      teams[teamIdx][pos] = {
        id: r.roleId,
        name: r.roleName,
        portraitId: r.portraitId || '',
        hp: r.hp,
        maxHp: r.hpMax,
        mana: 0,
        maxMana: 10,
        attack: r.attack,
        passives: r.passiveId && r.passiveId !== '无' ? [r.passiveId] : [],
        skills: r.skillId && r.skillId !== '无' ? [r.skillId] : [],
        buffs: {},
        buffList: [],
        alive: true,
        hpPercent: 100
      }
    }
    return { teams, events: [] }
  }

  function parseInitFromLog(logText) {
    const firstLine = logText.split('\n')[0] || ''
    const m = firstLine.match(/^Init:(.+)/)
    return m ? m[1].trim() : null
  }
  function initState(roomData) {
    const teams = [[], []]
    for (const teamIdx of [0, 1]) {
      const teamKey = teamIdx === 0 ? 'teamA' : 'teamB'
      const teamData = roomData[teamKey] || []
      for (const item of teamData) {
        const role = item[0]
        if (role) {
          teams[teamIdx].push({
            id: role.roleId,
            name: role.roleName,
            portraitId: role.portraitId || '',
            hp: role.hp,
            maxHp: role.hpMax,
            mana: 0,
            maxMana: 10,
            attack: role.attack,
            passives: role.passiveId && role.passiveId !== '无' ? [role.passiveId] : [],
            skills: role.skillId && role.skillId !== '无' ? [role.skillId] : [],
            buffs: {},
            buffList: [],
            alive: true,
            hpPercent: 100
          })
        }
      }
    }
    return { teams, events: [] }
  }

  /**
   * 解析日志文本 → 步骤数组
   */
  function parseLog(logText, initialState) {
    const lines = logText.split('\n').filter(l => l.trim())
    const result = []
    let state = cloneState(initialState)

    for (const line of lines) {
      const event = parseLine(line)
      if (event) {
        state = cloneState(state)
        applyEvent(event, state)
        result.push({ text: line, type: event.type, state: cloneState(state) })
      }
    }
    return result
  }

  function stepBack() {
    if (currentStep.value > 0) currentStep.value--
  }

  function stepForward() {
    if (currentStep.value < steps.value.length - 1) currentStep.value++
  }

  function stepTo(n) {
    currentStep.value = Math.max(0, Math.min(n, steps.value.length - 1))
  }

  function autoPlay() {
    if (autoPlaying.value) {
      clearInterval(autoPlayTimer)
      autoPlaying.value = false
    } else {
      autoPlaying.value = true
      autoPlayTimer = setInterval(() => {
        if (currentStep.value < steps.value.length - 1) {
          currentStep.value++
        } else {
          clearInterval(autoPlayTimer)
          autoPlaying.value = false
        }
      }, 300 / parseInt(localStorage.getItem('gameSpeed') || '1'))
    }
  }

  function isActive(team, pos) {
    if (!currentAction.value) return false
    return currentAction.value.team === team && currentAction.value.pos === pos
  }

  watch(currentStep, (newVal) => {
    if (steps.value.length === 0) return
    for (let i = newVal; i >= 0; i--) {
      if (steps.value[i].type === 'action') {
        const text = steps.value[i].text
        const m = text.match(/Action:(\d)阵营(\d)号位在行动/)
        if (m) {
          currentAction.value = { team: parseInt(m[1]), pos: parseInt(m[2]) }
          return
        }
      }
      if (steps.value[i].type === 'gameover') {
        gameOver.value = true
        currentAction.value = null
        return
      }
    }
    gameOver.value = false
  })

  watch(currentStep, async () => {
    await nextTick()
    if (logContainer.value) {
      const currentEl = logContainer.value.querySelector('.log-current')
      if (currentEl) currentEl.scrollIntoView({ behavior: 'smooth', block: 'center' })
    }
  })

  async function startBattle() {
    if (!roleInput.value.trim()) {
      ElMessage.warning('请输入角色编号')
      return
    }
    loading.value = true
    try {
      const { sessionId: sid } = await startAndPlay(roleInput.value.trim())
      sessionId.value = sid
      const logText = await getLog(sid)

      const initInput = parseInitFromLog(logText)
      if (!initInput) throw new Error('日志中未找到队伍初始化信息')

      const inputIds = initInput.split(/\s+/).map(Number)
      const rolesData = await getRolesByIds(inputIds)
      const initialState = initStateFromInput(rolesData, initInput)

      steps.value = parseLog(logText, initialState)
      currentStep.value = 0
      currentAction.value = null
      gameOver.value = false
      ElMessage.success(`加载成功，共 ${steps.value.length} 步`)
    } catch (e) {
      ElMessage.error('战斗失败: ' + (e.message || '未知错误'))
    } finally {
      loading.value = false
    }
  }

  async function loadBattle() {
    if (!sessionId.value.trim()) {
      ElMessage.warning('请输入 Session ID')
      return
    }
    loading.value = true
    try {
      const logText = await getLog(sessionId.value.trim())

      const initInput = parseInitFromLog(logText)
      if (!initInput) throw new Error('日志中未找到队伍初始化信息')

      const inputIds = initInput.split(/\s+/).map(Number)
      const rolesData = await getRolesByIds(inputIds)
      const initialState = initStateFromInput(rolesData, initInput)

      steps.value = parseLog(logText, initialState)
      currentStep.value = 0
      currentAction.value = null
      gameOver.value = false
      ElMessage.success(`加载成功，共 ${steps.value.length} 步`)
    } catch (e) {
      ElMessage.error('加载失败: ' + (e.message || '未知错误'))
    } finally {
      loading.value = false
    }
  }

  function toggleLog() {
    showLog.value = !showLog.value
  }

  return {
    sessionId, roleInput, loading, currentStep, autoPlaying, showLog, steps,
    currentAction, gameOver, logContainer,
    visibleSteps, currentState, currentEvent,
    startBattle, loadBattle, toggleLog, stepBack, stepForward, stepTo, autoPlay, isActive, getEffect
  }
}