// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {

	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	console.log('Congratulations, your extension "glassthought-sandbox" is now active!');

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with registerCommand
	// The commandId parameter must match the command field in package.json
	const disposable = vscode.commands.registerCommand('glassthought-sandbox.helloWorld', () => {
		// The code you place here will be executed every time your command is executed
		// Display a message box to the user
		vscode.window.showInformationMessage('Hello World from glassthought-sandbox!');
	});

	const showSystemInfo = vscode.commands.registerCommand('glassthought-sandbox.showSystemInfo', () => {
		const osInfo = `OS: ${process.platform} ${process.arch}\nNode: ${process.version}\nVSCode: ${vscode.version}`;
		vscode.window.showInformationMessage(`System Information:\n${osInfo}`, { modal: true });
	});

	const showProjectStats = vscode.commands.registerCommand('glassthought-sandbox.showProjectStats', () => {
		const workspaceFolders = vscode.workspace.workspaceFolders;
		const folderCount = workspaceFolders ? workspaceFolders.length : 0;
		const openEditors = vscode.window.visibleTextEditors.length;
		
		vscode.window.showInformationMessage(
			`Project Statistics:\n• Workspace folders: ${folderCount}\n• Open editors: ${openEditors}`,
			{ modal: true }
		);
	});

	const showQuickTip = vscode.commands.registerCommand('glassthought-sandbox.showQuickTip', () => {
		const tips = [
			"💡 Use Ctrl+Shift+P to open the Command Palette",
			"💡 Press F5 to start debugging your extension",
			"💡 Use Ctrl+` to toggle the integrated terminal",
			"💡 Press Ctrl+B to toggle the sidebar visibility",
			"💡 Use Alt+Click for multi-cursor editing"
		];
		const randomTip = tips[Math.floor(Math.random() * tips.length)];
		vscode.window.showInformationMessage(randomTip);
	});

	const showAbout = vscode.commands.registerCommand('glassthought-sandbox.showAbout', () => {
		vscode.window.showInformationMessage(
			'Glassthought Sandbox Extension v0.0.1\n\nA demonstration extension showcasing VSCode menu contributions.',
			'OK',
			'View Documentation'
		).then(selection => {
			if (selection === 'View Documentation') {
				vscode.env.openExternal(vscode.Uri.parse('https://code.visualstudio.com/api'));
			}
		});
	});

	context.subscriptions.push(disposable, showSystemInfo, showProjectStats, showQuickTip, showAbout);
}

// This method is called when your extension is deactivated
export function deactivate() {}
