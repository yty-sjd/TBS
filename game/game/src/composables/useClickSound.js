import { clickSoundUrl } from '../config/cdn'

let audio = null

export function useClickSound() {
  if (!audio) {
    audio = new Audio(clickSoundUrl())
  }

  function playClick() {
    if (audio.paused) {
      audio.currentTime = 0
      audio.play().catch(() => {})
    } else {
      audio.currentTime = 0
    }
  }

  function setVolume(vol) {
    if (audio) {
      audio.volume = vol
    }
  }

  return { playClick, setVolume }
}