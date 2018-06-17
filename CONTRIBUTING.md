Contributing to AIMA-JAVA
==========================
:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:  
If you'd like to report a bug or join in the development
of aima-java, then here are some notes on how to do that.

## Contents
* [Reporting bugs and opening issues](#reporting-bugs-and-opening-issues)
* [Coding Guidelines](#coding-guidelines)
    * [Pull Requests](#pull-requests)
    * [Git Commit Messages](#git-commit-messages)
* [Gitter Chat Room](#aima-chat-room)
* [Performance](#performance)
    
## Reporting bugs and opening issues

If you'd like a report a bug or open an issue then please:

**Check if there is an existing issue.** If there is then please add
   any more information that you have, or give it a 👍.

When submitting an issue please describe the issue as clearly as possible, including how to
reproduce the bug, which situations it appears in, what you expected to happen, and what actually happens.
If you can include a few test cases then that would be very helpful.

## Coding Guidelines

### Pull Requests
We love pull requests, so be bold with them! Don't be afraid of going ahead
and changing something, or adding a new feature. We're very happy to work with you
to get your changes merged into aima-java.

If you've got an idea for a change then please discuss it in the open first, 
either by opening an issue, or by joining us in our
[gitter chat room](https://gitter.im/aimacode/Lobby).

If you're looking for something to work on, have a look at the open issues in the repository [here](https://github.com/aimacode/aima-java/issues).

#### Pull Request Process
 1. Always work on your own fork of the repository and open pull requests when necessary.
 1. Ensure any install or build dependencies are removed before the end of the layer when doing a build.
 1. When working on a bug/issue , open a pull request as soon as you start working and mark
 it as `[WIP]` (Work in Progress), so that the developers can keep an eye on the work going on.
 1. Update the README.md with details of changes to the interface, this includes new environment variables, new classes, algorithms, useful file locations and container parameters.
 1. You may merge the Pull Request in once you have the sign-off of the maintainers, or if you do not have permission to do that, you may request the maintainer to do it for you.




### Git Commit Messages
* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally
* When only changing documentation, include `[ci skip]` in the commit description
* Consider starting the commit message with an applicable emoji:
    * :art: `:art:` when improving the format/structure of the code
    * :racehorse: `:racehorse:` when improving performance
    * :non-potable_water: `:non-potable_water:` when plugging memory leaks
    * :memo: `:memo:` when writing docs
    * :bug: `:bug:` when fixing a bug
    * :fire: `:fire:` when removing code or files
    * :green_heart: `:green_heart:` when fixing the CI build
    * :white_check_mark: `:white_check_mark:` when adding tests
    * :lock: `:lock:` when dealing with security
    * :arrow_up: `:arrow_up:` when upgrading dependencies
    * :arrow_down: `:arrow_down:` when downgrading dependencies
    * :shirt: `:shirt:` when removing linter warnings

## AIMA Chat Room

If you want to ask any questions in real-time, or get a feel for what's going on
then please drop into our [gitter public chat room](https://gitter.im/aimacode/Lobby).
If no one is online then you can still leave a message that will hopefully get a reply
when we return.

## Performance

Please do not make performance related improvements directly to the core code. This project is highly educational in nature. The main aim of the project
is to give concrete implementation of the algorithms stated in the book. Therefore, 
efficiency is compromised at times for the sake of similarity with the book. However, we maintain an extras module where such implementations
are welcome.