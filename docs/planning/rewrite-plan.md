# picMicroView rewrite plan

*In this file "the user" is the person Claude works with, and "Claude" is the assistant. Worked in
rolling-wave mode (`~/.claude/ROLLING_WAVE_MODE.md`).*

The rewrite keeps what picMicroView does (a PIC18F452 simulator for learning: step through
instructions and watch registers, memory and flags change) and rebuilds how it does it: modern
Java, a web UI instead of Swing, and tests checked against the datasheet.

## Current position

*Updated 2026-10-30.*

- **Where the work stands:** planning has started on `master`. Decisions D1–D10 are made (D8–D10
  settle part of step 2; D10's host setup is not done yet). No new code exists yet. The Swing code under `src/` is the behaviour reference (D1). Step 1 is done.
- **Next:** step 2 (choose the stack).
- **Order of the coming steps:** 2, 3, 4, then the core in 5–9; the UI (10) after the core can
  run a program headless.
- **Open decisions waiting on the user:**
  - the rest of the stack (step 2): build tool, web framework, how the browser gets updates,
    test tools;
  - where the old code lives during the rewrite (step 2): left in `src/` beside a new module, or
    moved to a `legacy/` folder;
  - the name of the first working branch, chosen once step 2 settles what comes first.
- **Stale documents (do not trust):** `README.md` (describes the Swing version 0.7), `doc/`
  (Javadoc from 2015, old package names), `tests/instructionTest/insTestDefault` (unreadable,
  see F2).

## Decisions

| # | Decision | Why |
|---|---|---|
| D1 | Rewrite, not refactor: same behaviour, modern Java, web UI. The Swing code is the reference for behaviour, not the design to keep. | The 2015 design is built around Swing and around reaching the host's hardware clock; neither carries over. |
| D2 | Table-driven decoder: each instruction declares its datasheet opcode pattern as mask/value; a 65,536-entry table is built at startup, checks that no patterns overlap, and maps unused words to an "unimplemented" handler. Handlers hold no state; decoded operands are passed in. | Matches how the chip decides (by opcode bits), each pattern can be checked line by line against the datasheet, overlaps fail at startup, and nothing is allocated per instruction. |
| D3 | Instruction-accurate simulator with a cycle counter; no attempt to reach the host's hardware clock. | Not possible from the JVM and not needed: correct timing comes from counting the PIC's instruction cycles. |
| D4 | Simulated time is the master clock. Timers and pin changes advance by cycles (with the datasheet's prescalers); pacing to wall-clock time is optional. | Timing is then exact and repeatable on any machine; the host clock is used only for pacing. |
| D5 | Port I/O goes through an interface (working name `PortBridge`); every pin change carries its cycle time stamp. | New outputs (screen, USB adapter, microcontroller bridge) are added as new implementations without touching the core. |
| D6 | First output: LEDs on screen paced to real time without drift (each frame shows the state for the cycle that matches the time). Toggling faster than the screen can show is drawn dimmed by duty cycle with a frequency readout; the UI shows when the simulator runs slower than real time. | A 1-second blink stays exact for as long as it runs; the screen's ~17 ms frame time is invisible and never accumulates. |
| D7 | Physical hardware output is possible later, not in scope now. | On-screen timing serves the teaching goal; hardware needs a separate bridge board for µs precision. |
| D8 | The build file is the single source of truth for building, running and testing (Java version, source folders, dependencies, test command). Editor files (`.vscode/`, `.idea/`) hold only conveniences such as launch configurations and recommended extensions. | The Swing version is defined only by IntelliJ's `.iml` and cannot be built outside IntelliJ; the user has since moved to VS Code. Claude runs builds and tests from the terminal, so they must work there exactly as in the editor. The project should still build years from now. |
| D9 | No dev container for now; reproducibility comes from the build (Maven Wrapper pinning the Maven version, the Enforcer plugin checking the JDK version). Revisit if the chosen Java version is not installed on the host or other contributors join. | The stack is a JDK and a build tool with no services to run. A dev container adds friction under the global rules (file permissions in `~/.claude`, git safe-directory reads, port forwarding) and can be added later as one file without code changes. |
| D10 | Java 25 (LTS), with no preview features. The build selects JDK 25 through Maven Toolchains and fails fast through a Maven Enforcer `requireJavaVersion` rule; the host's default JDK stays 21. | Chosen for longevity (the newer LTS, supported years longer than 21; tooling has had a year to catch up) and as a trial before the user's other project upgrades. Preview features can change between releases and would undo the longevity. Toolchains keep the choice in the build (D8) and leave other projects on the host untouched. JDK 25 sits beside 11, 17 and 21 under `/usr/lib/jvm`, so D9's revisit condition does not call for a dev container. |

## Steps

1. `[x]` **Project setup** (095cf86): the project's `CLAUDE.md` (shape from
   `~/.claude/MULTI_PROJECT_SETUP.md` step 3) and `project-state.yaml` with
   `working_mode: rolling-wave`.
2. `[ ]` **Choose the stack:** Java version, build tool, web framework, browser update channel
   (for example WebSocket or server-sent events), test tools for each level, and where the old
   code lives during the rewrite. Recorded as decisions. Already settled: D8 (the build file is
   the single source of truth), D9 (no dev container for now) and D10 (Java 25).
   Host setup for D10, run by the user once (Claude shows each command): install
   `openjdk-25-jdk` with apt; check with `update-alternatives` that `java` and `javac` still
   default to 21 and reset them if not; create `~/.m2/toolchains.xml` listing JDK 21 and 25; add
   both JDKs to VS Code's `java.configuration.runtimes` user setting.
3. `[ ]` **Reference sources:** the PIC18F452 datasheet's instruction set and memory chapters as
   the authority for behaviour (a pointer, not a copy in the repo); an inventory of what the Swing
   version implements and which `examples/` exist for each instruction.
4. `[ ]` **Build skeleton:** an empty project that builds, runs one test of each level, and is
   wired into the commands in `CLAUDE.md`.
5. `[ ]` **Program loading:** Intel HEX parsing into program memory, with tests for malformed
   input.
6. `[ ]` **Data memory:** general-purpose RAM, special function registers, banking (BSR) and the
   access bank, indirect addressing (FSR/INDF and its variants).
7. `[ ]` **Decoder (D2):** opcode table from the datasheet, overlap check, tests per pattern.
8. `[ ]` **Instructions:** semantics and status flags group by group, each tested against the
   datasheet; the `examples/` programs as scenario tests. Cycle counts per instruction (D3).
9. `[ ]` **Execution and time:** step and run, cycle counter, Timer0 with prescaler (D4).
10. `[ ]` **Web UI:** program and data memory views, the `.lst` view with the current line,
    changed-register highlighting, step/run controls, on-screen LEDs (D5, D6).

Later, not yet planned: other timers and peripherals, an editor and assembler integration (the
README's original plan), the hardware bridge (D7).

## Findings

Found in the Swing code on 2026-10-30, from reading it; to be checked against the datasheet in
step 8 where marked.

- **F1:** the only test (`TestMain`) records which address each instruction wrote and saves it
  through a Swing dialog; nothing compares a later run with the saved file.
- **F2:** `tests/instructionTest/insTestDefault` is Java-serialized with the class name
  `net.sourceforge.picmicroview.model.TestInstruction`; the package is now
  `picmicroview.model`, so it can no longer be read.
- **F3:** `Addwf.execute()` updates C, Z and N but not DC and OV, although helpers for both exist
  in `PicInstruction`. The datasheet lists C, DC, Z, OV, N as affected.
- **F4 (to check):** when ADDWF writes an FSRxL register with a carry, the code increments FSRxH.
  Claude believes the real chip only sets C.
- **F5:** `Clock.start()` runs a busy loop on the calling thread and increments Timer0 once per
  instruction; `Timer` overflows at `0x10000` whatever Timer0's configuration. Replaced by D3/D4.
