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
- The Swing version: `src/picmicroview/` (model, view, controller).
- Example programs: `examples/`, with `.asm`, `.hex`, `.lst` and `.cod` files per instruction and
  per concept.

## Commands

The Swing version has no build tool. It is an IntelliJ project (`picMicroView.iml`, language level
JDK 17, output to `out/`), run from IntelliJ with the main class in
`src/picmicroview/PicMain.java`. The rewrite's build and run commands come with plan step 4.

## Testing

No automated tests exist yet (plan finding F1). The test tools for each level of the global
`TESTING_APPROACH.md` are chosen in plan step 2.

## Project rules

- **The Swing code is the reference for behaviour, not the design to keep** (plan D1). Where it
  and the PIC18F452 datasheet disagree, the datasheet wins.
  *Why:* the rewrite keeps what the program does and rebuilds how; the old code has known flag
  errors (plan F3, F4).
- **Instruction semantics and status flags are checked against the datasheet,** never against
  the old code's output alone.
  *Why:* the program teaches how the chip behaves, so accuracy matters more than speed.
