[![build status](https://travis-ci.org/Locke/coreasm.core.svg?branch=locke)](https://travis-ci.org/Locke/coreasm.core) [![maven-central: de.athalis.coreasm:coreasm-parent](https://img.shields.io/maven-central/v/de.athalis.coreasm/coreasm-parent)](https://search.maven.org/search?q=g:de.athalis.coreasm%20a:coreasm-parent)

About
=====

This repository is forked from https://github.com/CoreASM/coreasm.core. CoreASM is licensed under the Academic Free License version 3.0, which can be found in the `LICENSE.md` file and is also available on http://www.opensource.org/licenses/afl-3.0.php. All changes in this repository are licensed under the Academic Free License version 3.0 as well.

This repository contains changes to the CoreASM Engine that have been created to better support a CoreASM-based S-BPM interpreter and a semantic verification.

The separate releases are necessary, as important bug fixes were only published as SNAPSHOT version (see [Issue 24](https://github.com/CoreASM/coreasm.core/issues/24)) and important Pull Requests are still without any feedback. Until the currently open PRs are not merged I won't create additional PRs upstream.



Branches
--------

The branch [upstream](https://github.com/Locke/coreasm.core/tree/upstream) follows the master branch of CoreASM. The [master](https://github.com/Locke/coreasm.core/tree/master) branch follows `upstream`, adds this README and a custom SNAPSHOT version.

Feature branches are based on `upstream`, and may be rebased when `upstream` is updated.

- [jparsec3](https://github.com/Locke/coreasm.core/tree/jparsec3), Pull Request at [PR29](https://github.com/CoreASM/coreasm.core/pull/29)
  - updates the minimum required Java version to 8
  - updates jParsec to version 3.0 and makes some limited use of Java 8 lambdas
- [fixes](https://github.com/Locke/coreasm.core/tree/fixes), Pull Request at [PR30](https://github.com/CoreASM/coreasm.core/pull/30)
  - various small fixes, performance improvements and other changes
- [emError-deadlock](https://github.com/Locke/coreasm.core/tree/emError-deadlock), Pull Request at [PR31](https://github.com/CoreASM/coreasm.core/pull/31)
  - resolves an assertion error
  - add blocking wait for next command in emError
- [codestyle](https://github.com/Locke/coreasm.core/tree/codestyle)
  - various code improvements
- [ForkJoinPool](https://github.com/Locke/coreasm.core/tree/ForkJoinPool), Pull Request pending
  - migrate from `EDU.oswego.cs.dl.util.concurrent` to `java.util.concurrent`, as it has been deprecated and merged into Java 7, where support continues
  - Exceptions are no longer caught and stored, but use the native error handling
  - as `java.util.concurrent.ForkJoinPool` no longer offers stats a custom measurement of execution times has been added
  - batched execution ([recursive] evaluation of multiple agents in one tasks) has been removed, as the ForkJoinPool can better distribute the work across workers
  - TODO: determine number of processors to be used

The [locke](https://github.com/Locke/coreasm.core/tree/locke) branch contains a somewhat stable version of features that are likely to be released next. Its history may be rewritten to pull in different features and pick commits from the development branch.

There are some additional major differences to upstream:
- targets Java 11
- the maven layout has been changed
  - the eclipse modules are not yet supported

Development of new features happens in the [dev](https://github.com/Locke/coreasm.core/tree/dev) branch, which is based on the `locke` branch. It must be considered as experimental, as it contains experiments and patches for my specific workload. I share them for the interested visitor, but be warned that the history is dirty and will be rewritten a lot (i.e. expect things that I try out but discard later on).

Upstream Merged PRs
-------------------

- [nanoTime](https://github.com/Locke/coreasm.core/tree/nanoTime), Pull Request at [PR27](https://github.com/CoreASM/coreasm.core/pull/27)
  - adds the function `nanoTime`, which serves `System.nanoTime()`, to better measure elapsed time than the `now` function, which is based on `System.currentTimeMillis()`
- [test-jar](https://github.com/Locke/coreasm.core/tree/test-jar), no Pull Request intended
  - installing / publishing `org.coreasm.engine` now creates an additional test-jar, to make it possible to use the `TestEngineDriver` from external projects
- [avoid-yield](https://github.com/Locke/coreasm.core/tree/avoid-yield), Pull Request at [PR26](https://github.com/CoreASM/coreasm.core/pull/26)
  - uses a signal to wait for the Engine to be idle; the Engine uses a `LinkedBlockingQueue` to wait for a new command when it is idle. This improves CPU utilization as waiting threads no longer call `Thread.yield()` constantly in a loop until the condition changes
  - `TestEngineDriver` has been restructured to avoid an internal thread, on which was waited with an additional `Thread.yield()`-loop
  - this branch is based on `test-jar`, which should've been a separate PR


Releases
--------

Releases are tagged with the suffix `-locke-N`. Their history will not be rewritten.

- [1.7.3-locke-3](https://github.com/Locke/coreasm.core/releases/tag/v1.7.3-locke-3) at 2019-09-23
- [1.7.3-locke-2](https://github.com/Locke/coreasm.core/releases/tag/v1.7.3-locke-2) at 2019-02-07
- [1.7.3-locke-1](https://github.com/Locke/coreasm.core/releases/tag/locke-1) at 2018-10-04
