# picMicroView

What this file is: what is true of this project alone, loaded only in this repository; the
shared rules are in `~/.claude/CLAUDE.md`; "the user" is the person Claude works with.

## About

A simulator of the PIC18F452 microcontroller for learning: the user loads an assembled program,
steps through it one instruction at a time and watches registers, memory and status flags change.

## Java

@~/.claude/JAVA_STANDARDS.md

This import is from outside the repository: the user approves it once from the terminal CLI,
started in the project folder; the VS Code extension skips it silently until then.

## Where things are

- Active plan: `docs/planning/rewrite-plan.md` (rolling-wave mode; its Current position comes
  first).
- Session state: `project-state.yaml` at the repository root.
- The Swing version: `legacy/src/picmicroview/` (model, view, controller), tagged `swing-final`
  (plan D19).
- The datasheet: `reference/39564c.pdf` (git-ignored; download link in the plan, step 3). The
  inventory of instructions, Swing classes and examples: `docs/planning/inventory.md`.
- Example programs: `examples/`, with `.asm`, `.hex`, `.lst` and `.cod` files per instruction and
  per concept.

## Commands

The Swing version has its own Maven build, outside the main build (Java 17, the examples on its
classpath):

- Build: `./mvnw -f legacy/pom.xml package` (from the repository root)
- Run: `java -jar legacy/target/picmicroview-legacy.jar`

The rewrite's build and run commands come with plan step 4.

## Testing

No automated tests exist yet (plan finding F1). The test tools for each level of the global
`TESTING_APPROACH.md` are chosen in plan D18; how to run them comes with plan step 4.

## Project rules

- **The Swing code is the reference for behaviour, not the design to keep** (plan D1). Where it
  and the PIC18F452 datasheet disagree, the datasheet wins.
  *Why:* the rewrite keeps what the program does and rebuilds how; the old code has known flag
  errors (plan F3, F4).
- **Instruction semantics and status flags are checked against the datasheet,** never against
  the old code's output alone.
  *Why:* the program teaches how the chip behaves, so accuracy matters more than speed.
