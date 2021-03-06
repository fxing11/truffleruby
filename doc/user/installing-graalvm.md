# Using TruffleRuby with GraalVM

[GraalVM](http://graalvm.org/) is the platform on which TruffleRuby runs.

## Dependencies

[TruffleRuby's dependencies](../../README.md#dependencies) need to be installed
for TruffleRuby to run correctly.

## Community Edition and Enterprise Edition

GraalVM is available in a Community Edition, which is open-source, and an
Enterprise Edition which has better performance and scalability.
See https://www.graalvm.org/downloads for a comparison.

## Installing the Base Image

GraalVM starts with a base image which provides the platform for
high-performance language runtimes.

The Community Edition base image can be installed from GitHub, under an open
source licence.
The Enterprise Edition base image can only be installed from the Oracle
Technology Network using the OTN licence.

See https://www.graalvm.org/downloads for download links.

Nightly builds of the GraalVM Community Edition are
[also available](https://github.com/graalvm/graalvm-ce-dev-builds/releases).

Whichever edition you get you will get a tarball which you can extract. There
will be a `bin` directory (`Contents/Home/bin` on macOS) which you can add to
your `$PATH` if you want to.

## Installing Ruby and Other Languages

After installing GraalVM you then need to install the Ruby language into it.
This is done using the `gu` command. The Ruby package is the same for both
editions of GraalVM and comes from GitHub.

```bash
$ gu install ruby
```

This command will show a message mentioning to run a post-install script.
This is necessary to make the Ruby openssl C extension work with your system libssl.
Please run that script now.
The path of the script will be:
```bash
# Java 8
jre/languages/ruby/lib/truffle/post_install_hook.sh
# Java 11+
languages/ruby/lib/truffle/post_install_hook.sh
# Generic
$(path/to/graalvm/bin/ruby -e 'print RbConfig::CONFIG["prefix"]')/lib/truffle/post_install_hook.sh
```

You can also download the Ruby component (`ruby-installable-...`) manually from
https://github.com/oracle/truffleruby/releases. Then install it with
`gu install --file path/to/ruby-installable-...`.

If you are installing Ruby into GraalVM EE then you need to download the Ruby
EE installable from OTN and install using `--file` in the same way.

After installing Ruby you may want to rebuild other images so that they can
use the new language. Rebuilding the executable images can take a few minutes
and you should have about 10 GB of RAM available.

```bash
$ gu rebuild-images polyglot libpolyglot
```

To be able to do so, you may need to install the `native-image` component if you
haven't done so already:

```bash
gu install native-image
```

## Using a Ruby Manager

Inside the GraalVM is a `jre/languages/ruby` or `languages/ruby` directory which
has the usual structure of a Ruby implementation. It is recommended to add this
directory to a Ruby manager, see [configuring Ruby managers](ruby-managers.md)
for more information.
