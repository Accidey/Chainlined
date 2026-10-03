# ChainMailed

> **Mod Introduction / 模组介绍**

---

## English

### Overview

**ChainMailed** is a Minecraft **NeoForge** mod for **Minecraft 1.21.1** that delivers a comprehensive rework of vanilla chainmail armor. While preserving the vanilla visual style, it introduces a brand-new **Chainmail Lining** mechanic, along with the all-new **Impact Resistance** attribute, allowing chainmail armor to truly shine in combat.

This mod is a **port** of the original *ChainMailed* project — created by **AstianBk** and **SangreBK** for **Minecraft 1.20.1 (Forge)** — adapted to the **NeoForge** platform by **Accidey**.

### Core Features

- **Chainmail Lining System**
  - Hold any armor piece in your main hand and a piece of chainmail armor for the same slot in your offhand, then right-click to attach the chainmail as a lining to the armor.
  - The lined armor gains additional armor points and Impact Resistance on top of its original defense.
  - The lining can be removed at any time: sneak and right-click while your offhand is empty.
  - When a lined armor piece is destroyed, the chainmail lining is automatically returned to the corresponding equipment slot.
  - Armor pieces with a lining display a **"Chainmailed"** tooltip.

- **Impact Resistance Attribute**
  - A new attribute granted by chainmail armor and lined armor.
  - When taking melee attacks, player attacks, or generic damage, it reduces the incoming damage by a fixed amount — but always leaves at least **1 point** of damage, so it never grants complete immunity.
  - Also applies to certain mobs equipped with chainmail, such as skeletons, zombies, and piglins.

- **Full Set Bonus: Sweep Immunity**
  - When wearing a full set of chainmail or fully lined armor, the wearer is immune to player **sweeping attacks**.

- **Crafting Recipes**
  - Vanilla chainmail armor pieces can now be crafted using **iron nuggets** and **chains**.

- **New Advancement**
  - Added the **"Chainmailed!"** advancement under the vanilla *Story* tab for obtaining chainmail armor.

- **New Sound Effect**
  - A dedicated **chainmail block sound** plays when chainmail stops an attack.

- **Configuration Options**
  - Toggle the chainmail block sound.
  - Adjust the Impact Resistance value granted per armor piece.
  - Configure base armor values per slot.
  - Maintain a blacklist of armor pieces that cannot receive a lining.

- **In-Game Commands**
  - `/chainmailed blacklist add <item>` — add an item to the lining blacklist (persisted to the config file, effective immediately).
  - `/chainmailed blacklist remove <item>` — remove an item from the lining blacklist (persisted to the config file, effective immediately).
  - The command can also operate directly on the item held in your main hand.

### Credits

**ChainMailed** was originally created by **AstianBk** and **SangreBK**.

This is a **NeoForge port** by **Accidey**. We would like to express our sincere gratitude to the original authors for their outstanding work.

### Requirements

- Minecraft **1.21.1**
- **NeoForge** 21.1.249 or later
- Java **21**

### Installation

1. Install the required **NeoForge** version.
2. Place the downloaded `ChainMailed` mod JAR into the `mods` folder.
3. Launch Minecraft and enjoy.

---

## 中文

### 模组简介

**ChainMailed** 是一个基于 **Minecraft 1.21.1** 的 **NeoForge** 模组，旨在为原版锁链甲带来一次全面的重做。模组在保留原版视觉风格的前提下，引入了全新的**锁链内衬**机制与全新的**冲击抗性**属性，让锁链甲在战斗中真正发挥其应有的作用。

本模组为原版 *ChainMailed* 项目——由 **AstianBk** 与 **SangreBK** 为 **Minecraft 1.20.1（Forge）** 创作——由 **Accidey** 移植适配至 **NeoForge** 平台。

### 核心特性

- **锁链内衬系统**
  - 主手持任意护甲、副手持同部位的锁链甲，右键即可将锁链甲作为内衬加装到该护甲上。
  - 加衬后的护甲将在原有防御基础上获得额外护甲值与冲击抗性。
  - 内衬可随时取下：副手空手时潜行并右键即可。
  - 当加衬的护甲损坏时，锁链内衬会自动返还至对应装备栏位。
  - 已加衬的护甲会显示**「已加锁链内衬」**的提示文本。

- **冲击抗性属性**
  - 由锁链甲及加衬护甲提供的新属性。
  - 受到近战攻击、玩家攻击或通用伤害时，将按固定数值减免伤害——但至少保留 **1 点**伤害，因此永远不会完全免伤。
  - 同时适用于部分穿着锁链甲的生物，如骷髅、僵尸与猪灵等。

- **全套加成：横扫免疫**
  - 当穿戴全套锁链甲或全套加衬护甲时，穿戴者免疫玩家的**横扫攻击**。

- **合成配方**
  - 原版锁链甲部件现在可通过**铁粒**与**锁链**合成获得。

- **新进度**
  - 在原版「冒险」进度选项卡下新增了**「锁链内衬！」**进度，以奖励获得锁链甲的玩家。

- **新音效**
  - 新增专属的**锁链甲格挡音效**，在锁链甲挡下攻击时播放。

- **配置选项**
  - 可开关锁链甲格挡音效。
  - 可调整每件护甲提供的冲击抗性数值。
  - 可配置各部位的护甲基础数值。
  - 可维护无法加装锁链内衬的护甲黑名单。

- **游戏内指令**
  - `/chainmailed blacklist add <物品>` —— 将物品加入内衬黑名单（写入配置文件并立即生效）。
  - `/chainmailed blacklist remove <物品>` —— 将物品移出内衬黑名单（写入配置文件并立即生效）。
  - 指令也可直接作用于主手持有的物品。

### 致谢

**ChainMailed** 的原作者为 **AstianBk** 与 **SangreBK**。

本项目为 **Accidey** 制作的 **NeoForge 移植版本**。在此，我们衷心感谢原作者所做出的卓越工作。

### 运行环境

- Minecraft **1.21.1**
- **NeoForge** 21.1.249 及以上版本
- Java **21**

### 安装方法

1. 安装对应版本的 **NeoForge**。
2. 将下载好的 `ChainMailed` 模组 JAR 文件放入 `mods` 文件夹。
3. 启动游戏即可体验。