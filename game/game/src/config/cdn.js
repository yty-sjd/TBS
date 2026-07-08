// d:\暂时文件\实训\Game\game\game\src\config\cdn.js
const CDN_BASE = 'https://tbs-yty.oss-cn-guangzhou.aliyuncs.com'

export function avatarUrl(assistantId) {
  return `${CDN_BASE}/avatar/${assistantId || 30086009}.webp`
}

export function bgUrl(filename) {
  return `${CDN_BASE}/background/${filename}`
}

export function roleBgUrl(assistantId) {
  return `${CDN_BASE}/rolebackground/${assistantId}.webp`
}

export function startBgUrl(filename) {
  return `${CDN_BASE}/startbackground/${filename}`
}

export function clickSoundUrl() {
  return `${CDN_BASE}/click/click.mp3`
}

export function homepageBgmUrl(filename) {
  return `${CDN_BASE}/Homepage_bgm/${filename || '1'}.mp3`
}