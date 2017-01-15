# pclbox [![Build Status](https://travis-ci.org/michaelknigge/pclbox.svg?branch=master)](https://travis-ci.org/michaelknigge/pclbox) [![codecov.io](https://codecov.io/github/michaelknigge/pclbox/coverage.svg?branch=master)](https://codecov.io/github/michaelknigge/pclbox?branch=master) [![Coverity Status](https://scan.coverity.com/projects/10949/badge.svg)](https://scan.coverity.com/projects/10949)

Java library for parsing [PCL](https://en.wikipedia.org/wiki/Printer_Command_Language) printer data streams. The library supports PCL up to version 5 as well as [PJL](https://en.wikipedia.org/wiki/Printer_Job_Language) and [HP/GL2](https://en.wikipedia.org/wiki/HPGL). PCL version 6 (aka PCL-XL) is currently not supported.

Note that the support of HP/GL2 is somehow limited. The HP/GL commands need to be terminated by a semicolon, which is not enforced by the specification.

# Dependencies
pclbox has no runtime dependencies on other libraries. This was a design decision and will (hopefully) never change.

# Usage
You need to create a `PrinterCommandHandler` and pass this, together with a `File` or `InputStream`, to the constructor of the `PclParser`. Then invoke the method `parse` of the `PclParser`. This will parse the complete PCL data stream.

For every parsed command, the method `handlePrinterCommand` of the `PrinterCommandHandler` gets invoked. This method receives a `PrinterCommand` that can be passed to an own implementation of the `PrinterCommandVisitor`.

# Contribute
If you want to contribute to pclbox, you're welcome. But please make sure that your changes keep the quality of pclbox at least at it's current level. So please make sure that your contributions comply with the pclbox coding conventions (formatting etc.) and that your contributions are validated by JUnit tests.

It is easy to check this - just build the source with `gradle` before creating a pull request. The gradle default tasks will run [checkstyle](http://checkstyle.sourceforge.net/), [findbugs](http://findbugs.sourceforge.net/) and build the JavaDoc. If everything goes well, you're welcome to create a pull request.

Hint: If you use Eclipse as your IDE, you can simply run `gradle eclipse` to create the Eclipse project files. Furthermore you can import Eclipse formatter settings (see file `config/eclipse-formatter.xml`) that will assis you in formatting the pclbox source code according the used coding conventions used (no tabs, UTF-8 encoding, indent by 4 spaces, no line longer that 120 characters, etc.).

# Manuals
The following reference materials were used to implement this parser:

* [PCL 5e Technical Quick Reference Guide](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13205/bpl13205.pdf)
* [PCL 5e Technical Reference Manual Part 1](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13210/bpl13210.pdf)
* [PCL 5e Technical Reference Manual Part 2](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13211/bpl13211.pdf)
* [PCL 5 Comparison Guide](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13206/bpl13206.pdf)
* [PCL 5 Comparison Guide Addendum](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13209/bpl13209.pdf)
* [PCL 5 Color Technical Reference Manual](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13212/bpl13212.pdf)
* [HP-GL/2 reference](http://www.hpmuseum.net/document.php?catfile=213)
* [HP PCL/PJL Reference: Printer Job Language Technical Reference Manual](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13208/bpl13208.pdf)
* [HP PCL/PJL Reference: Printer Job Language Technical Reference Addendum](http://h20000.www2.hp.com/bc/docs/support/SupportManual/bpl13207/bpl13207.pdf)
