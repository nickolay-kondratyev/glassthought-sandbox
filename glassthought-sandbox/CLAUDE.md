# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a VSCode extension called "glassthought-sandbox" built with TypeScript. The extension uses the VSCode Extension API and is bundled with esbuild.

## Essential Commands

### Development
```bash
# Install dependencies
npm install

# Compile, lint, and build
npm run compile

# Watch mode (auto-recompile on changes)
npm run watch

# Run linter only
npm run lint

# Type check only
npm run check-types
```

At the end run `release-install.sh` which rebuilds and installs the extension in VSCode.

### Testing
```bash
# Run all tests
npm test

# Debug extension: Press F5 in VSCode to launch Extension Development Host
```

### Building & Packaging
```bash
# Production build
npm run package

# Package extension into .vsix file
npx vsce package

# Install packaged extension
code --install-extension glassthought-sandbox-*.vsix
```

## Architecture

### Entry Points
- **src/extension.ts**: Main extension entry point containing `activate()` and `deactivate()` functions
- All commands must be registered in the `activate()` function

### Command Registration Pattern
```typescript
// In activate() function:
let disposable = vscode.commands.registerCommand('glassthought-sandbox.commandName', () => {
    // Command implementation
});
context.subscriptions.push(disposable);
```

### Build Process
1. TypeScript compilation (ES2022 target, strict mode enabled)
2. ESLint validation with TypeScript rules
3. esbuild bundles to CommonJS format in `dist/extension.js`
4. VSCode module is marked as external dependency

### Extension Manifest
Commands and contributions are defined in `package.json` under:
- `contributes.commands`: Command definitions
- `contributes.menus`: Menu contributions (command palette, editor context, etc.)
- `activationEvents`: When the extension loads (currently no specific events)

## Key Configuration Files
- **tsconfig.json**: TypeScript config with strict mode, ES2022 target
- **esbuild.js**: Custom build configuration for bundling
- **eslint.config.mjs**: ESLint setup with TypeScript support

## Current Tasks
The file `ask.dnc.md` indicates the next task is to add commands to VSCode's top-level menu with subgrouping functionality.
