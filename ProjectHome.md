# Applino #

## A new way to deploy Java Desktop Applications ##

  * An applino is to the desktop what a servlet is to the server.
  * Applino provides and environment for running multiple Java desktop applications within a single JVM.
  * The Applino environment runs as a taskbar icon and starts when the user logs into their computer.
  * Applini (Applino applications) are similar to servlets in that they contain runtime descriptors and have their life cycles dictated by the Applino container.

Most Java developers realise that it is inefficient to instantiate a separate JVM for each Java application running on a user's computer. This involves a significant extra start-up time and is wasteful of memory. Unfortunately, due to the need to segregate the effects of one application from another there hasn't been any choice.

On the server side however, much progress has been made in the area of Java Application Servers which are able to run multiple web applications within the same JVM. The level of intra application protection is so good that several hosting companies provide shared hosting where users host their web applications in the same JVM as other users.

The idea behind this project is simple: take the compartmentalisation technology which has been developed for servlets and reapply it to the desktop space. This has proven to be exceedingly simple as concepts such as context life cycles and hierarchical class loaders map perfectly to the requirements of a mono JVM desktop environment.

Typically _porting_ an existing application to the Applino environment is trivial. It is simply a matter of ensuring that it is properly started when instructed and properly terminated. The initial window for the hosted application will also be provided by the Applino container (so that it can control events such as window closing).

As a demonstration of how easy it is to port existing applications, we have already provided Applino versions of all the JFC example applications from the JDK.

**http://www.applino.com/**

Also see our sister project: UIDL (User Interface Description Language):
**http://www.uidl.net/**

## Documentation ##

  * [Documentation](http://www.applino.com/doc/index.html) (single page HTML format)

## Download ##

Main Applino container is available from [Google code](http://code.google.com/p/applino/downloads/list).

**Note: Applino requires JRE 1.6 or later.**

### Download Applini! ###

Download sample applications which have been converted into Applini. This simply involves adapting them to the Applino lifecycle, which resembles a servlet lifecycle. Most of these examples are adapted versions of the "JFC" demo applications provided with the Sun JDK (in the `demo/jfc` directory).

Simply download an applino below and drag into the "Applini" folder on your desktop. You need to have an applino container installed first (see download section above).

  * [Font Test](http://java.uidl.net/applini/FontTest.aar)
  * [Password Store](http://java.uidl.net/applini/PasswordStore.aar)
  * [Java 2D](http://java.uidl.net/applini/Java2D.aar)
  * [Swing Set 2](http://java.uidl.net/applini/SwingSet2.aar)
  * [Notepad](http://java.uidl.net/applini/Notepad.aar)
  * [Stylepad](http://java.uidl.net/applini/Stylepad.aar)
  * [Simple Text](http://java.uidl.net/applini/SimpleText.aar)
  * [Calculator](http://java.uidl.net/applini/Calculator.aar)
  * [Simple Applino](http://java.uidl.net/applini/SimpleApplino.aar)

## Participate! ##

  * [Applino @ Google Groups](http://groups.google.com/group/applino)