# SprykerKit - intellij plugin for spryker file generation integrated into your ide

[![CI](https://github.com/nexusunited/spryker-kit-intellij-plugin/actions/workflows/main.yml/badge.svg)](https://github.com/nexusunited/spryker-kit-intellij-plugin/actions/workflows/main.yml)
[![Deploy](https://github.com/nexusunited/spryker-kit-intellij-plugin/actions/workflows/deploy.yml/badge.svg)](https://github.com/nexusunited/spryker-kit-intellij-plugin/actions/workflows/deploy.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/18766-sprykerkit.svg)](https://plugins.jetbrains.com/plugin/18766-sprykerkit)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18766-sprykerkit.svg)](https://plugins.jetbrains.com/plugin/18766-sprykerkit)

<!-- Plugin description -->
Supports the development of [Spryker](https://www.spryker.com) applications with Intellij IDEA.

Features:

* Create Spryker Module boilerplate based on your current context
* Create Spryker Files and Folders based on your current context
* Supported Layers Client, Glue, Service, Shared, Yves, Zed
* Configure desired project namespace

<!-- Plugin description end -->

## Info
- File templates in src/main/resources/templates (downloaded from https://github.com/spryker-sdk/spryk/tree/master/config/spryk/templates)

## ToDo list
- [ ] Add method generation
- [ ] Add new Type `Method` i.e. usecase -> add dependency (context *DependencyProvider.php)
- [ ] Add new Type `Recipe` i.e. usecase -> add config/config_default.php variable AND add src/Zed/Module/ModuleConfig.php getter

## References
 - https://github.com/nexusunited/spryk/tree/feature/templates-lite
 - https://github.com/tobi812/idea-php-spryker-plugin#2-generate-spryker-classes
 - https://github.com/turbine-kreuzberg/spryker-idea-plugin

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij_spryker_plugin"</kbd> >
  <kbd>Install Plugin</kbd>


