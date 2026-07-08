import { reactive, ref } from 'vue'

export const gameState = reactive({
  phase: 'none',
  roomId: null,
  userId: null,
  mySide: 0,
  myUsername: '',
  myAssistantId: 30086009,
  opponentUsername: '',
  opponentAssistantId: 30086009,
  myPicks: [],
  roleIds: '',
  sessionId: '',
  role: 'host',
  backToLobby: false
})

export const isTransitioning = ref(false)

export function doPhaseTransition(newPhase) {
  isTransitioning.value = true
  setTimeout(() => {
    gameState.phase = newPhase
  }, 400)
}