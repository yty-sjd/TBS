const SKILL_MAP = {
  'Archmage': '5sp  进行一次攻击力为8的攻击',
  'military_advisor': '2sp  前置位角色与其前置位角色交换位置',
  'Electric Mouse': '3sp    给予攻击对象1层晕眩',
  'Destroyer': '2sp 	给予攻击对象1层放逐（跳过回合，不会成为效果对象和攻击对象）',
  'assassin': '2sp	进行3次攻击力为1的攻击',
  'pharmacist_all': '2sp	回复我方阵营全部角色2点生命值',
  'power bank': '2sp	我方阵营技力+1',
  'viper': '2sp	给予攻击对象2层剧毒',
  'pharmacist': '2sp    回复前置位角色4点生命值'
}

const PASSIVE_MAP = {
  'dote_on': '游戏开始时，1号位获得状态：每次受到伤害时，如果伤害大于2则降至1，宠溺失去一点生命值',
  'radiation': '游戏开始时，后置位角色获得状态：技能对象改为对方阵营全部角色或我方阵营全部角色',
  'sloth': '后置位角色代替进行行动',
  'accelerate': '后置位角色技力+5',
  'scattering': '该角色不会攻击，游戏开始时后置位角色获得状态：攻击对象改为对方阵营全部角色',
  'change': '对位敌人与1号位敌人交换位置',
  'conceal': '后置位队友4回合内不会成为攻击，效果对象',
  'stealing_heaven': '复制对位角色被动及技能',
  'pretentiousness': '复制前置位角色被动，后置角色技能',
  'piece': '获得我方阵营其他角色的被动',
  'gradual': '每次攻击后攻击力+1',
  'frost': '对方阵营1号位角色每次受到大于1的伤害时，再受到一次1点伤害 （不可叠加）',
  'follow_up': '对方阵营每次获得负面状态时，状态层数+1',
  'Magic': '受到的伤害-1',
  'greed': '无生命上限',
  'revenge': '每次受到伤害反伤1点伤害',
  'scaveng': '继承阵亡角色被动及技能（角色只能拥有一个技能）',
  'CounterPassive': '反击'
}

export function skillName(id) {
  const key = Object.keys(SKILL_MAP).find(k => k.toLowerCase() === id.toLowerCase())
  return key ? SKILL_MAP[key] : id
}

export function passiveName(id) {
  const key = Object.keys(PASSIVE_MAP).find(k => k.toLowerCase() === id.toLowerCase())
  return key ? PASSIVE_MAP[key] : id
}

const BUFF_MAP = {
  'buff_dizziness': '晕眩',
  'buff_poison': '剧毒',
  'buff_notBecomeATargetOfSkill': '免疫技能',
  'buff_notBecomeATargetOfAttack': '免疫攻击',
  'buff_notAttack': '无法攻击',
  'buff_groupAttack': '群体攻击',
  'buff_damageAttackPlus': '攻击成长',
  'buff_damageReturn': '反伤',
  'buff_damageMinus': '减伤',
  'buff_damageReplace': '替伤',
  'buff_damageReplacePlus': '替伤递增',
  'buff_replaceAction': '替行',
  'buff_layerPlus': '层数增长',
  'buff_noHpLimit': '无血限',
  'buff_replaceSkill': '技能扩散',
  'buff_exile': '放逐'
}

export function buffName(key) {
  return BUFF_MAP[key] || key.replace('buff_', '')
}