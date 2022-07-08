# SprykerKit - intellij plugin for spryker file generation integrated into your ide

## ToDo list
- [x] Create a new plugin to generated spryker files context based (Namespace: Pyz)
- [x] Adjust docs
- [x] Setup CI/CD
- [ ] Publish Plugin
- [ ] Add method generation
- [x] Add plugin configurations (i.e. Namespaces)
- [ ] Add new Type `Method` i.e. usecase -> add dependency (context *DependencyProvider.php)
- [ ] Add new Type `Recipe` i.e. usecase -> add config/config_default.php variable AND add src/Zed/Module/ModuleConfig.php getter

## Info
 - File templates in src/main/resources/templates (downloaded from https://github.com/spryker-sdk/spryk/tree/master/config/spryk/templates)

## References
 - https://github.com/nexusunited/spryk/tree/feature/templates-lite
 - https://github.com/tobi812/idea-php-spryker-plugin#2-generate-spryker-classes
 - https://github.com/turbine-kreuzberg/spryker-idea-plugin

![Build](https://github.com/patrickjaja/intellij_spryker_plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
Supports the development of [Spryker](https://www.spryker.com) applications with Intellij IDEA.

Features:

 * Create Spryker Module boilerplate based on your current context
 * Create Spryker Files and Folders based on your current context
 * Supported Layers Client, Glue, Service, Shared, Yves, Zed
 * Configure desired project namespace

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij_spryker_plugin"</kbd> >
  <kbd>Install Plugin</kbd>


