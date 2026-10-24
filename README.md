# PROJECT-SLOTMACHINE
This is our project about slotmachine in java ENJOY :D.

Build and run the project with JDK 25 (the current LTS release).

## Folders
- `src/` the code, in two packages:
  - `domain/` the slot machine: `SlotMachine`, the wheels (`Wheel`, `Rebel`, `Lefty`, `WildWheel`), the symbols (`Symbol`, `Shy`, `Ephemeral`), `SlotMachineContest`, the factories that create wheels and symbols by type (`WheelFactory`, `SymbolFactory`), and the drawing classes of the course (`Canvas`, `Circle`, `Rectangle`, `Triangle`).
  - `test/` unit tests of each cycle (`SlotMachineC1Test` ... `SlotMachineC4Test`) and the visual acceptance tests.
- `bin/` the compiled classes (made by Maven or VS Code).
- `lib/` the libraries (JUnit), copied by Maven.

To open it in BlueJ, open the `src` folder.

## Maven
- `mvn clean verify` compiles into `bin`, copies JUnit into `lib`, runs the unit tests, the coverage check (75% of the domain) and PMD.
- Coverage report: `target/site/jacoco/index.html`.
- PMD report: `mvn pmd:pmd`, then `target/reports/pmd.html`.
