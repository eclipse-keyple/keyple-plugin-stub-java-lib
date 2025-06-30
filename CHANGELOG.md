# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- Logging improvement.
### Changed
- Migrated the CI pipeline from Jenkins to GitHub Actions.

## [2.2.1] - 2024-04-12
### Changed
- Java source and target levels `1.6` -> `1.8`
### Upgraded
- Gradle `6.8.3` -> `7.6.4`
- Keyple Plugin API `2.2.0` -> `2.3.0`
- Keyple Util Library `2.3.1` -> `2.4.0`

## [2.2.0] - 2023-11-30
### Added
- Added project status badges on `README.md` file.
### Fixed
- CI: code coverage report when releasing.
### Upgraded
- Keyple Plugin API `2.0.0` -> `2.2.0`
- Keyple Util Library `2.1.0` -> `2.3.1`

## [2.1.0] - 2022-06-03
### Added
- "CHANGELOG.md" file (issue [eclipse-keyple/keyple#6]).
- CI: Forbid the publication of a version already released (issue [#7]).
- New way of mocking APDU responses for a `StubSmartCard` using a provider (issue [#10]).
### Upgraded
- "Keyple Util Library" to version `2.1.0` by removing the use of deprecated methods.

## [2.0.0] - 2021-10-06
This is the initial release.
It follows the extraction of Keyple 1.0 components contained in the `eclipse-keyple/keyple-java` repository to dedicated repositories.
It also brings many major API changes.

[unreleased]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/compare/2.2.1...HEAD
[2.2.1]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/compare/2.2.0...2.2.1
[2.2.0]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/compare/2.1.0...2.2.0
[2.1.0]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/releases/tag/2.0.0

[#10]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/issues/10
[#7]: https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib/issues/7

[eclipse-keyple/keyple#6]: https://github.com/eclipse-keyple/keyple/issues/6