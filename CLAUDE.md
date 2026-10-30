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
- Time log: `docs/time-log.csv` (active minutes per day), kept by the user's time-report tool
  (see Time tracking).

## Commands

The Swing version has its own Maven build, outside the main build (Java 17, the examples on its
classpath):

- Build: `./mvnw -f legacy/pom.xml package` (from the repository root)
- Run: `java -jar legacy/target/picmicroview-legacy.jar`

The rewrite's build and run commands come with plan step 4.

## Testing

The test tools for each level of the global `TESTING_APPROACH.md` are chosen in plan D18. So far
`core` has unit and architecture tests, run with `./mvnw test`; the full set of commands comes
with plan step 4f. The old Swing version has no usable tests (plan F1).

## Time tracking

The user wants to know how many active hours the project takes, in total and per week.

- **At every pause** (in addition to `~/.claude/PAUSE_CONVENTION.md`), Claude runs
  `python3 ~/.claude/tools/time-report/time_report.py --weekly` from the repository root,
  commits `docs/time-log.csv` with the state snapshot, and shows its summary and weekly lines
  (weeks run Monday to Sunday, the user's choice) in the pause output.
- The tool is the user's, shared by all projects and versioned in `~/.claude`; it is not part of
  this project. Only the log is.
- Active time comes from the timestamps in Claude Code's session records: gaps up to 7 minutes
  count as work, longer gaps as breaks (the user's choice on 2026-10-30, from measured reply
  times: reading and answering took under 7–8 minutes, breaks 14 minutes or more). All or
  nothing: a longer gap counts zero. Time spent without a Claude session is not counted.
- Claude Code deletes session records after `cleanupPeriodDays` (set to 365 in the user's global
  settings on 2026-10-30); the log keeps every day already computed, so it survives that.
  *Why:* a lasting record of the effort, from the first session (2026-10-29) to the end.

## Project rules

- **The Swing code is the reference for behaviour, not the design to keep** (plan D1). Where it
  and the PIC18F452 datasheet disagree, the datasheet wins.
  *Why:* the rewrite keeps what the program does and rebuilds how; the old code has known flag
  errors (plan F3, F4).
- **Instruction semantics and status flags are checked against the datasheet,** never against
  the old code's output alone.
  *Why:* the program teaches how the chip behaves, so accuracy matters more than speed.
