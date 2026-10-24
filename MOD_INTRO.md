# Chainlined

> **Mod Introduction / 模组介绍**

---

## English

### Overview

**Chainlined** is a Minecraft **NeoForge** mod for **Minecraft 1.21.1** that delivers a comprehensive rework of vanilla chainmail armor. While preserving the vanilla visual style, it introduces a brand-new **Chainmail Lining** mechanic, along with the all-new **Impact Resistance** attribute, allowing chainmail armor to truly shine in combat.

This mod is an original creation by **Accidey**.

### Core Features

- **Chainmail Lining (Anvil)**
  - Put any armor piece in the left anvil slot and a chainmail piece for the same slot in the right slot, then take the lined armor out of the anvil.
  - The right-slot chainmail is consumed, and a small amount of experience levels is paid (1 by default, configurable).
  - The lined armor gains additional armor points and Impact Resistance on top of its original defense.
  - Armor pieces with a lining display a **"Chainmailed"** tooltip.
  - Once an armor piece is lined, it cannot be lined again; the lining is permanent and cannot be manually removed.

- **Lining Inheritance (Crafting Table)**
  - When a lined armor piece is used as an ingredient to craft an upgraded armor piece of the same slot on a crafting table, the lining is automatically preserved on the crafted result.
  - For example, upgrading a lined diamond chestplate to a netherite chestplate keeps the chainmail lining.

- **Impact Resistance Attribute**
  - A new attribute granted by chainmail armor and lined armor.
  - When taking melee attacks, player attacks, or generic damage, it reduces the incoming damage by a fixed amount — but always leaves at least **1 point** of damage, so it never grants complete immunity.
  - Also applies to certain mobs equipped with chainmail, such as skeletons, zombies, and piglins.

- **Crafting Recipes**
  - Vanilla chainmail armor pieces can now be crafted using **iron nuggets** and **chains**.

- **New Advancements**
  - **"Chainmailed!"** — obtain chainmail armor.
  - **"Lined in Steel"** — line an armor piece with chainmail on an anvil.

- **New Sound Effect**
  - A dedicated **chainmail block sound** plays when chainmail stops an attack.

- **Configuration Options**
  - Toggle the chainmail block sound.
  - Adjust the Impact Resistance value granted per armor piece.
  - Configure base armor values per slot.
  - Configure the anvil experience cost (default 1, range 0 ~ 10000).
  - Maintain a blacklist of armor pieces that cannot receive a lining.

- **In-Game Commands**
  - `/chainlined blacklist add <item>` — add an item to the lining blacklist (persisted to the config file, effective immediately).
  - `/chainlined blacklist remove <item>` — remove an item from the lining blacklist (persisted to the config file, effective immediately).
  - The command can also operate directly on the item held in your main hand.

### Credits

**Chainlined** was created by **Accidey**.

### Requirements

- Minecraft **1.21.1**
- **NeoForge** 21.1.249 or later
- Java **21**

### Installation

1. Install the required **NeoForge** version.
2. Place the downloaded `Chainlined` mod JAR into the `mods` folder.
3. Launch Minecraft and enjoy.

---

## 中文

### 模组简介

**Chainlined** 是一个基于 **Minecraft 1.21.1** 的 **NeoForge** 模组，旨在为原版锁链甲带来一次全面的重做。模组在保留原版视觉风格的前提下，引入了全新的**锁链内衬**机制与全新的**冲击抗性**属性，让锁链甲在战斗中真正发挥其应有的作用。

本模组由 **Accidey** 原创开发。

### 核心特性

- **锁链内衬（铁砧）**
  - 在铁砧左槽放入任意护甲、右槽放入同部位的锁链甲，即可从铁砧取出加衬护甲。
  - 右槽的锁链甲会被消耗，并支付少量经验等级（默认 1 级，可配置）。
  - 加衬后的护甲将在原有防御基础上获得额外护甲值与冲击抗性。
  - 已加衬的护甲会显示**「已加锁链内衬」**的提示文本。
  - 护甲一旦加衬便无法再次加衬；内衬为永久效果，无法手动取下。

- **内衬继承（工作台）**
  - 当使用加衬护甲作为材料，在工作台合成同部位的升级护甲时，内衬会自动保留在合成结果上。
  - 例如：将加衬的钻石胸甲升级为下界合金胸甲，锁链内衬会一并保留。

- **冲击抗性属性**
  - 由锁链甲及加衬护甲提供的新属性。
  - 受到近战攻击、玩家攻击或通用伤害时，将按固定数值减免伤害——但至少保留 **1 点**伤害，因此永远不会完全免伤。
  - 同时适用于部分穿着锁链甲的生物，如骷髅、僵尸与猪灵等。

- **合成配方**
  - 原版锁链甲部件现在可通过**铁粒**与**锁链**合成获得。

- **新进度**
  - **「锁链内衬！」** —— 获得锁链甲。
  - **「钢甲衬链」** —— 在铁砧上为护甲加装锁链内衬。

- **新音效**
  - 新增专属的**锁链甲格挡音效**，在锁链甲挡下攻击时播放。

- **配置选项**
  - 可开关锁链甲格挡音效。
  - 可调整每件护甲提供的冲击抗性数值。
  - 可配置各部位的护甲基础数值。
  - 可配置铁砧加衬的经验费用（默认 1，范围 0 ~ 10000）。
  - 可维护无法加装锁链内衬的护甲黑名单。

- **游戏内指令**
  - `/chainlined blacklist add <物品>` —— 将物品加入内衬黑名单（写入配置文件并立即生效）。
  - `/chainlined blacklist remove <物品>` —— 将物品移出内衬黑名单（写入配置文件并立即生效）。
  - 指令也可直接作用于主手持有的物品。

### 致谢

**Chainlined** 由 **Accidey** 制作。

### 运行环境

- Minecraft **1.21.1**
- **NeoForge** 21.1.249 及以上版本
- Java **21**

### 安装方法

1. 安装对应版本的 **NeoForge**。
2. 将下载好的 `Chainlined` 模组 JAR 文件放入 `mods` 文件夹。
3. 启动游戏即可体验。