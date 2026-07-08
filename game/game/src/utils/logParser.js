import { buffName } from '../config/skillNames'

/**
 * 解析一行日志，返回事件对象
 * @param {string} line 日志行
 * @returns {object|null} 事件对象 { type, ... }
 */
export function parseLine(line) {
  let m

  m = line.match(/^Init:(.+)/)
  if (m) return { type: 'init', ids: m[1].trim() }

  m = line.match(/Action:(\d)阵营(\d)号位在行动/)
  if (m) return { type: 'action', team: parseInt(m[1]), pos: parseInt(m[2]) }

  m = line.match(/Passive:(\d)阵营(\d)号位使用被动(.+)/)
  if (m) return { type: 'passive', team: parseInt(m[1]), pos: parseInt(m[2]), passive: m[3] }

  m = line.match(/buff:(\d)阵营(\d)号位(buff_\w+)\s+(-?\d+)/)
  if (m) return { type: 'buff', team: parseInt(m[1]), pos: parseInt(m[2]), buff: m[3], value: parseInt(m[4]) }

  m = line.match(/add_passive:(\d)阵营(\d)号位获得(\d)阵营(\d)号位被动效果/)
  if (m) return { type: 'add_passive', team: parseInt(m[1]), pos: parseInt(m[2]), fromTeam: parseInt(m[3]), fromPos: parseInt(m[4]) }

  m = line.match(/add_skill:(\d)阵营(\d)号位获得(\d)阵营(\d)号位技能(.*)/)
  if (m) return { type: 'add_skill', team: parseInt(m[1]), pos: parseInt(m[2]), fromTeam: parseInt(m[3]), fromPos: parseInt(m[4]), skill: m[5].trim() }

  m = line.match(/skill:(\d)阵营(\d)号位(.+)/)
  if (m) return { type: 'skill', team: parseInt(m[1]), pos: parseInt(m[2]), skill: m[3] }

  m = line.match(/PositionSwap:(\d)阵营(\d)号位与\1阵营(\d)号位交换位置/)
  if (m) return { type: 'swap', team: parseInt(m[1]), posA: parseInt(m[2]), posB: parseInt(m[3]) }

  m = line.match(/attack:(\d)阵营(\d)号位attack\s+(\d)阵营(\d)号位\s+(\d+)点伤害值/)
  if (m) return { type: 'attack', team: parseInt(m[1]), pos: parseInt(m[2]), targetTeam: parseInt(m[3]), targetPos: parseInt(m[4]), damage: parseInt(m[5]) }

  m = line.match(/damage:(\d)阵营(\d)号位受到(\d+)点伤害/)
  if (m) return { type: 'damage', team: parseInt(m[1]), pos: parseInt(m[2]), amount: parseInt(m[3]) }

  m = line.match(/change_hp:(\d)阵营(\d)号位(-?\d+)/)
  if (m) return { type: 'hp', team: parseInt(m[1]), pos: parseInt(m[2]), value: parseInt(m[3]) }

  m = line.match(/change_attack:(\d)阵营(\d)号位(-?\d+)/)
  if (m) return { type: 'attack_change', team: parseInt(m[1]), pos: parseInt(m[2]), value: parseInt(m[3]) }

  m = line.match(/change_currentMana:(\d)阵营(\d)号位(\d+)/)
  if (m) return { type: 'mana', team: parseInt(m[1]), pos: parseInt(m[2]), value: parseInt(m[3]) }

  m = line.match(/checkSurvival:(\d)阵营(\d)号位阵亡/)
  if (m) return { type: 'death', team: parseInt(m[1]), pos: parseInt(m[2]) }

  if (line.startsWith('Game Over') || line.startsWith('GameOver')) {
    const winMatch = line.match(/(\d)阵营获胜/)
    const drawMatch = line.match(/最大回合数/)
    return { type: 'gameover', text: line, winner: winMatch ? parseInt(winMatch[1]) : null, draw: !!drawMatch }
  }

  m = line.match(/执行被动(.+)/)
  if (m) return { type: 'exec_passive', passive: m[1] }

  return null
}

/**
 * 将事件应用到状态上（原地修改 state）
 */
export function applyEvent(event, state) {
  const role = (team, pos) => state.teams[team]?.[pos]

  switch (event.type) {
    case 'action':
      break

    case 'hp':
      if (role(event.team, event.pos)) {
        role(event.team, event.pos).hp = event.value
        const r = role(event.team, event.pos)
        r.hpPercent = Math.max(0, Math.round((r.hp / r.maxHp) * 100))
      }
      break

    case 'mana':
      if (role(event.team, event.pos)) {
        role(event.team, event.pos).mana = event.value
      }
      break

    case 'attack_change':
      if (role(event.team, event.pos)) {
        role(event.team, event.pos).attack = event.value
      }
      break

    case 'death':
      if (role(event.team, event.pos)) {
        role(event.team, event.pos).alive = false
        role(event.team, event.pos).hp = 0
        role(event.team, event.pos).hpPercent = 0
      }
      break

    case 'swap': {
      const team = state.teams[event.team]
      if (team && team[event.posA] && team[event.posB]) {
        ;[team[event.posA], team[event.posB]] = [team[event.posB], team[event.posA]]
      }
      break
    }

    case 'add_passive':
      if (role(event.team, event.pos)) {
        const fromRole = role(event.fromTeam, event.fromPos)
        if (fromRole && fromRole.passives.length > 0) {
          const p = fromRole.passives[0]
          if (!role(event.team, event.pos).passives.includes(p)) {
            role(event.team, event.pos).passives.push(p)
          }
        }
      }
      break

    case 'add_skill':
      if (role(event.team, event.pos) && event.skill) {
        if (!role(event.team, event.pos).skills.includes(event.skill)) {
          role(event.team, event.pos).skills.push(event.skill)
        }
      }
      break

    case 'buff':
      if (role(event.team, event.pos)) {
        role(event.team, event.pos).buffs[event.buff] = event.value
        updateBuffList(role(event.team, event.pos))
      }
      break

    case 'gameover':
      break
  }
}

/**
 * 根据 buffs 对象生成 buffList 显示数组
 */
export function updateBuffList(role) {
  role.buffList = []
  for (const [key, val] of Object.entries(role.buffs)) {
    if (val > 0) {
      const label = buffName(key)
      let type = 'info'
      if (label.includes('Attack') || label.includes('groupAttack')) type = 'danger'
      else if (label.includes('Skill')) type = 'warning'
      else if (label.includes('notBecome')) type = 'success'
      role.buffList.push({ name: key, label: label + ':' + val, type })
    }
  }
}

/**
 * 深拷贝状态
 */
export function cloneState(state) {
  return {
    teams: state.teams.map(team => team.map(role => ({
      ...role,
      passives: [...role.passives],
      skills: [...role.skills],
      buffs: { ...role.buffs },
      buffList: [...role.buffList]
    })))
  }
}