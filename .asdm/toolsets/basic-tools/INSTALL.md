# basic-tools

Development and Git workflow toolset for project bootstrapping and commit management.

## Toolset ID

`basic-tools`

## Actions

### start-project
Start the project in development mode by reading README.md instructions and verifying all services work correctly.

### git-commit-message
Generate a conventional git commit message by analyzing `git status` and `git diff` output.

### git-commit
Stage all changes (`git add -A`) and commit using a previously generated commit message. Requires a commit message to exist in the conversation context.

## Installation Steps

1. Create the following command files in `.codebuddy/commands/`:
   - `asdm-start-project.md` - Start project in development mode
   - `asdm-git-commit-message.md` - Generate git commit message
   - `asdm-git-commit.md` - Commit changes with generated message

2. Each command file should contain the corresponding action prompt content.

## Dependencies

- Project must have a `README.md` with startup instructions
- Git must be initialized in the project
