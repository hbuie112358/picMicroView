# picMicroView rewrite plan

*In this file "the user" is the person Claude works with, and "Claude" is the assistant. Worked in
rolling-wave mode (`~/.claude/ROLLING_WAVE_MODE.md`).*

The rewrite keeps what picMicroView does (a PIC18F452 simulator for learning: step through
instructions and watch registers, memory and flags change) and rebuilds how it does it: modern
Java, a web UI instead of Swing, and tests checked against the datasheet.

## Current position

*Updated 2026-10-30.*

- **Where the work stands:** planning on `master`. Steps 1–3 are done; decisions D1–D19 are
  made (D8–D19 are the stack; D15 partly replaces D13). JDK 25 is installed on the host beside
  21, which stays the default; the user's other project is pinned to JDK 21 on its own. No new
  code exists yet. The Swing code under `src/` is the behaviour reference (D1); it moves to
  `legacy/` in step 4 (D19).
- **Next:** step 4 (build skeleton). Step 3 is done: the datasheet is in `reference/` and the
  inventory in `inventory.md`.
- **Order of the coming steps:** 3, 4, then the core in 5–9; the UI (10) after the core can run a
  program headless; then write, assemble and run (11).
- **Open decisions waiting on the user:**
  - where assembling happens (step 11, options A, B, C); not needed before step 11, but option
    C would add an assembler to `core`, so it is best settled before step 7 (decoder, D2);
  - the name of the first working branch (needed by step 4, the first code).
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
| D11 | Maven is the build tool, run through the Maven Wrapper (`mvnw`); the Maven version is chosen in step 4 from the then-current release, not from the host's package (3.8.7). | The user already knows Maven and uses it in another project; `pom.xml` is declarative configuration, which suits D8; the build is small and needs none of Gradle's flexibility. D9 and D10 already rely on the Wrapper, Toolchains and Enforcer. |
| D12 | Spring Boot (4.x) is the web framework. Spring MVC or WebFlux is still to be chosen (settled by D17: MVC). | Chosen by the user to learn Spring, a goal of the project alongside fit. Spring's constructor injection matches the construction rule, and it brings externalized configuration, Actuator metrics, testing support and Spring Security (needed for D14). Java 25 support verified 2026-10-30: Spring Boot 4.0's system requirements say "compatible up to and including Java 25". Javalin fits the present server size slightly better; Quarkus would reuse the user's existing knowledge but teach less that is new. |
| D13 | Two Maven modules: `core` (the simulator, plain Java, no Spring dependency and no annotations) and `app` (Spring Boot, depends on `core`). Core objects are created explicitly in `@Bean` methods in `app`. | The core must run headless and be testable without a framework (D1–D5). With the boundary in the build, a Spring import in `core` does not compile. Explicit `@Bean` wiring keeps the most important wiring readable, which serves the user's goal of seeing what Spring does. **Partly replaced by D15:** the core runs in the browser, not inside `app`, so the `@Bean` wiring of core objects no longer applies; the two-module split and the no-Spring rule for `core` stand. |
| D14 | The app will later be deployed for other learners, with user logins. Not built now, but from step 5 on the core supports many independent simulator instances in one process: no static or global simulator state; one instance per user session. | The project is a learning tool for others too. Retrofitting multi-instance support into a core built around one global simulator would be a rewrite of its own. Note: a server-side simulator costs server CPU per running user (small when paced to real time, a whole core in a run-as-fast-as-possible mode); see step 2, "where the simulator runs". With D15 each user's simulator runs in their own browser, so server CPU per user no longer applies; the no-static-state rule stays (several simulators on one page, tests in parallel). |
| D15 | The simulator runs in the browser. The `core` module is cross-compiled by TeaVM (a build-time compiler from Java bytecode, run by Maven) to WebAssembly or JavaScript; which of the two is decided in step 4 (Claude leans to WebAssembly: real 32-bit integers for byte arithmetic). The Spring Boot server (`app`) serves the pages and the compiled core, and later logins and saved programs (D14). `core` uses only what TeaVM supports (no reflection, class loading or JNI; a limited standard library), and the TeaVM build enforces it. | The user's requirement: a light that should flash every half second must flash every half second whatever the network does; "late but correct" is not acceptable. A server-side simulator can guarantee that only for programs that repeat exactly; in the browser, the program (which is fixed) runs locally and the network matters only for pages, logins and saving. It also removes input lag on simulated buttons and server CPU per user. TeaVM chosen 2026-10-30 over GraalVM Web Image (experimental), CheerpJ (no Java 25 support) and J2CL (alpha, built for Google's tools): TeaVM 0.13 added Java 25 support, it is actively released (0.16 current) and has a Maven plugin. Output size not yet measured; TeaVM includes only the code actually used. Checked for real in step 4, not in a separate trial (the user's choice). |
| D16 | All Java tests use JUnit Jupiter (JUnit 5, or JUnit 6 with the same style; version chosen with the test tools in step 2). The compiled core is checked by end-to-end tests in a real browser, comparing against reference results written by the JVM tests, not by TeaVM's JUnit 4 browser runner. | JUnit 5's parameterized tests (datasheet tables of inputs and expected flags), dynamic tests (one test per `examples/` program) and nested tests suit this project; TeaVM's browser runner supports only JUnit 4. Browser end-to-end tests are needed for the UI anyway (step 10), so checking the compiled core there adds no new tool. |
| D17 | Spring MVC (not WebFlux), with virtual threads enabled (`spring.threads.virtual.enabled=true`, set in step 4). |
| D18 | Test tools: **JUnit 6** (Jupiter; settles D16's version) and **AssertJ** for all Java tests; **ArchUnit** in `core` to enforce no non-final static fields (D14) and no reflection (D15); **`spring-boot-starter-test`** (MockMvc, `@WebMvcTest`, `@SpringBootTest`) in `app`; **Playwright for Java** for browser end-to-end tests (D16); **PIT** mutation testing run on demand, not on every build; **Testcontainers** later, with the database (D14). Maven Surefire runs unit tests (`mvn test`), Failsafe runs integration and browser tests (`mvn verify`). | Checked 2026-10-30: Spring Boot 4.0.9 manages JUnit Jupiter 6.0.3 and AssertJ 3.27.7, so one JUnit version serves both modules; ArchUnit 1.4.1+ supports Java 25; Playwright for Java 1.64 is current and keeps tests in Java and Maven. PIT measures the user's rule that every test can fail for a real bug (it would report F3's missing DC/OV updates as surviving mutants). Not confirmed: PIT with JUnit 6, checked in step 4; if it fails, PIT waits. JaCoCo coverage left out on purpose (PIT answers the better question) and can be added later. If the UI (step 10) ends up in TypeScript with its own tests, Playwright's Node version is reconsidered then. |
| D19 | The Swing code moves to `legacy/` in step 4: first the current code is tagged (working name `swing-final`), then `src/`, `tests/` and `doc/` are moved with `git mv`. `legacy/` gets its own `pom.xml` (Java 17, as the `.iml` had), not part of the main build, so the old code stays navigable as Java in VS Code and can be built and run from the terminal. It is exempt from the new code's standards. `legacy/` is deleted once step 8 covers everything the old version did (confirmed with the user at that point). `examples/` stays at the root. | D1 keeps the old code as the behaviour reference, so it must stay readable and runnable; with a root Maven `pom.xml`, VS Code would no longer treat a root `src/` in its non-Maven layout as Java. The newest tag, `v0.75-beta` (2015), is 46 commits older than the current Swing code, which has no name in git. Its own `pom.xml` applies D8 to the old code, which today only builds in IntelliJ (`.idea/`, `.iml` and `out/` are git-ignored and local only). | After D15 every server need is short request/response work: static files and the compiled core, logins, saving and loading programs from a database, and possibly running an assembler (step 11, option A). None needs streaming or many slow back-end calls, WebFlux's strengths. MVC keeps the standard Spring Security setup and JPA over JDBC; WebFlux would need reactive Security and R2DBC for no gain. Virtual threads let requests wait on the database or an external process without holding a real thread, which meets the aim of constraint 11 (non-blocking IO) without reactive code. |

## Steps

1. `[x]` **Project setup** (095cf86): the project's `CLAUDE.md` (shape from
   `~/.claude/MULTI_PROJECT_SETUP.md` step 3) and `project-state.yaml` with
   `working_mode: rolling-wave`.
2. `[x]` **Choose the stack** (1584c51): Java version, build tool, web framework, where the simulator runs,
   test tools for each level, and where the old code lives during the rewrite. Recorded as
   decisions. Already settled: D8 (the build file is the single source of truth), D9 (no dev
   container for now), D10 (Java 25), D11 (Maven), D12 (Spring Boot), D13 (two modules), D15
   (simulator in the browser, compiled by TeaVM), D16 (JUnit Jupiter; browser end-to-end tests
   for the compiled core), D17 (Spring MVC with virtual threads), D18 (test tools) and D19 (the
   old code moves to `legacy/`). Nothing in step 2 is still open.
   Host setup for D10, done 2026-10-30 (JDK 25.0.4.1; java alternatives now in manual mode on
   21): install
   `openjdk-25-jdk` with apt; check with `update-alternatives` that `java` and `javac` still
   default to 21 and reset them if not; create `~/.m2/toolchains.xml` listing JDK 21 and 25; add
   both JDKs to VS Code's `java.configuration.runtimes` user setting.
   Where the simulator runs was settled by D15 (option B, in the browser). Option A (on the
   server, streaming cycle-stamped pin changes) was set aside: input reaches the simulator
   0.2–0.4 s late, it costs server CPU per user, and during an outage output is correct only
   for programs that repeat exactly.
3. `[x]` **Reference sources:** the PIC18F452 datasheet's instruction set and memory chapters as
   the authority for behaviour (a pointer, not a copy in the repo); an inventory of what the Swing
   version implements and which `examples/` exist for each instruction.
   **The datasheet:** Microchip *PIC18FXX2 Data Sheet*, DS39564C (2006, the current revision;
   332 pages; covers the PIC18F242/252/442/452). Downloaded 2026-10-30 from
   <https://ww1.microchip.com/downloads/aemDocuments/documents/MCU08/ProductDocuments/DataSheets/39564c.pdf>
   to `reference/39564c.pdf`, which git ignores (Microchip's copyright); SHA-256
   `1ad0654f42af3ab94360589595803fac273903b073b602297fe8ee20ac7bd1fe`. This is the long PDF the
   user calls "the manual". Printed page numbers are PDF page numbers minus 2. Chapters used:
   4.0 Memory Organization (page 35), 8.0 Interrupts (73), 9.0 I/O Ports (87), 10.0 Timer0
   (103), 20.0 Instruction Set Summary (211).
   **Inventory:** [`inventory.md`](inventory.md). The datasheet has 69 instructions (75 counting
   the `TBLRD`/`TBLWT` forms); the Swing version implements 59, all with examples; 10 are
   missing (`CALL`, `CLRWDT`, `POP`, `PUSH`, `RESET`, `RETFIE`, `RETLW`, `SLEEP`, `TBLRD`,
   `TBLWT`) and have no examples. No interrupt support.
4. `[ ]` **Build skeleton:** an empty project that builds, runs one test of each level, and is
   wired into the commands in `CLAUDE.md`. Includes the TeaVM build of `core` (D15) and one
   browser end-to-end test of the compiled output (D16); this is where TeaVM with Java 25 is
   first checked, where WebAssembly or JavaScript is chosen (confirming browser support), and
   where the output size is measured. Also the D19 move: tag the Swing code, `git mv` it to
   `legacy/`, add `legacy/pom.xml`. And F6: track `.gitignore`.
5. `[ ]` **Program loading:** Intel HEX parsing into program memory, with tests for malformed
   input.
6. `[ ]` **Data memory:** general-purpose RAM, special function registers, banking (BSR) and the
   access bank, indirect addressing (FSR/INDF and its variants).
7. `[ ]` **Decoder (D2):** opcode table from the datasheet, overlap check, tests per pattern.
8. `[ ]` **Instructions:** semantics and status flags group by group, each tested against the
   datasheet; the `examples/` programs as scenario tests. Cycle counts per instruction (D3).
   Test design (the user's direction, 2026-10-30): the main tests are readable edge-case tables
   from the datasheet (JUnit 6 parameterized tests), one row per case where something happens,
   for example 255 + 1 = 0 with C, Z and DC set; 0x0F + 0x01 with DC only; 127 + 1 with OV and
   N; plus one plain case showing no flag is set by mistake. An exhaustive check (every input,
   compared with the datasheet's flag rules written as simple formulas) is optional, decided
   per instruction group.
9. `[ ]` **Execution and time:** step and run, cycle counter, Timer0 with prescaler (D4).
10. `[ ]` **Web UI:** program and data memory views, the `.lst` view with the current line,
    changed-register highlighting, step/run controls, on-screen LEDs (D5, D6).
    **Requirement: robust and forgiving of an unstable network.** The user asked that this be
    considered throughout the design, not added at the end. With D15 the simulator and its
    output timing run locally, so the network matters only for what still crosses it: loading
    the page and compiled core (cached after the first visit), logins, and loading and saving
    programs (D14). For those: an info message (not an error) when the network is slow or
    down, and no lost work when a save fails. Details decided when that work is planned.
    Ideas raised before D15 for a server-to-browser event stream, kept in case a stream returns
    (for example the hardware bridge, D7): a heartbeat ("simulated up to cycle N, nothing
    changed"); each batch saying when the next change is due; proven repetition (identical
    complete chip state at cycles A and B means A→B repeats exactly until an input arrives, so
    the browser can replay it through an outage without guessing); batching fast toggling.

11. `[ ]` **Write, assemble and run:** the learner writes a `.asm` program in the browser,
    assembles it, runs it on the simulated chip and sees what it really does: assembler errors
    pointing at source lines; stepping with the current source line highlighted; LEDs, including
    the dimmed-with-frequency display for toggling too fast to see (D6). The user's goal is
    "working with the chip itself" (the README's original editor plan, raised to a step on
    2026-10-30).
    **Open: where assembling happens.** The `examples/` were assembled with gpasm 0.13.7 and use
    `#include p18f452.inc`, `list`, `cblock`, `org` and `__config`; each has gpasm's `.hex` and
    `.lst` saved.
    - *A, gpasm on the server:* matches the examples exactly. Every assemble needs the network;
      running a native tool on untrusted input from the internet needs a sandbox, timeouts,
      resource limits and protection against `#include` reading server files.
    - *B, gpasm compiled to WebAssembly,* running in the browser: gpasm is C; whether this has
      been done for gpasm is not yet checked.
    - *C, an own PIC18 assembler in `core`* (Java, compiled by TeaVM with the simulator): offline
      and instant, error messages written for learners, and the source-line-to-address map that
      step 10's source view needs. Can share D2's opcode table; gpasm's saved `.hex` for every
      example is a ready-made test. Claude leans to C; not chosen.

Later, not yet planned: other timers and peripherals, the hardware bridge (D7), deployment for
other learners with user logins and a database for accounts (D14).

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
- **F6:** `.gitignore` lists itself, so git does not track it: its rules (`.idea/`, `*.iml`,
  `out/`, and since 2026-10-30 `reference/`) exist only on this machine, and a fresh clone has
  none. To fix in step 4: remove the self-reference and commit `.gitignore` with the build's
  ignores (for example `target/`).
- **F7:** the datasheet's summary table (Table 20-2, printed page 214) has printing errors:
  ADDWF's encoding `0010 01da0` (one bit too many), ADDWFC's `0010 0da` (one bit short), BTG's
  operands `f, d, a` (should be `f, b, a`); confirmed on the page image, not an extraction error.
  The decoder (step 7, D2) takes opcode patterns from each instruction's own page in section 20.1,
  never from Table 20-2 alone.
- **F8:** `examples/basic/r/rlfnc` and `examples/inDepth/r/rlfnc` are misnamed: no instruction
  RLFNC exists; their programs exercise `RLNCF`. To rename (or merge) when the examples become
  scenario tests (step 8).
- **F9:** the Swing version's **Assemble** menu item has no action attached; the old version never
  assembled programs. Relevant to step 11.
