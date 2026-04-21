# spec-tools

Product specification generation toolset for creating Epic PRDs and Feature PRDs.

## Toolset ID

`spec-tools`

## Actions

### epic-generation
Generate an Epic-level Product Requirements Document (PRD) based on user input. Saves to `.asdm/spec/{seq}-{name}/epic.md`.

### epic2feature-generation
Generate a Feature-level PRD under an existing Epic. Saves to `.asdm/spec/{epic-seq}-{epic-name}/feat{feat-seq}-{feat-name}.md`.

## Installation Steps

1. Create the following command files in `.codebuddy/commands/`:
   - `asdm-epic-generation.md` - Generate Epic PRD
   - `asdm-epic2feature-generation.md` - Generate Feature PRD from Epic

2. Each command file should contain the corresponding action prompt content.

## Dependencies

- `.asdm/docs/asdm.instructions.md` must exist for project context
- `.asdm/spec/` directory must exist for output
