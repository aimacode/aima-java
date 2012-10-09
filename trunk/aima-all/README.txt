= AIMA3e-JAVA =
Notes By Ciaran (ctjoreilly@gmail.com), Ruediger (Ruediger.Lunde@gmail.com) and Ravi(magesmail@yahoo.com).

= Introduction =
The latest (and ever evolving) code can be found at:<br>

http://aima-java.googlecode.com/svn/trunk/

A quick overview on how to get set up and started using the AIMA-Java projects can be found at:<br>

http://code.google.com/p/aima-java/wiki/GettingStarted

== Project Organization ==
 * This project is now organized into 4 sub-projects:
  * aima-core : contains all the implemented algorithms described in AIMA3e.
  * aima-gui  : contains the GUI and command line demo applications. This project is dependent on aima-core.
  * aimax-osm : contains the Open Street Map (OSM) library and applications. This project is dependent on aima-core and aima-gui.
  * aima-all  : is the master project, used by aima-java developers, for creating releases.
 * Sub-Project directory organization is based on the standard [http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html Maven] directory layout.

== Current Release: 1.7.0-Chp4-Complete ==
1.7.0-Chp4-Complete : 09 Oct 2012 :<br>
Addition of And-Or-Search-Graph algorithm from chapter 4 and a Nondeterministic Vacuum Environment to the Vacuum GUI. 
Also, the Genetic Algorithm was reimplemented in order to make it easier to use/extend.
This is based on the following sub-project releases:
  * aima-core (0.10.5) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.8)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.2)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  

== Bug Report - Acknowledgments ==

The following people sent in excellent comments and bug reports. Thank you!!!!<br>
  * Kevin O'Connor, PhD student Heriot-Watt University, Issues 76 and 77<br>
  * Ramiro Varela, Associate Professor University of Oviedo, Issues 74 and 75<br>
  * mbilgic, Issue 73<br>
  * Federico Baron and Alessandro Daniele, University of Padua, Hybrid Wumpus Agent suggestions and code.<br>
  * Ken Stevens, Issue 70<br>
  * Asterios, Issue 67<br>
  * Betty Keren, Issue 66<br>
  * codesamu..., Issue 63<br>
  * Jose Samaniego, suggestions for compiling and running from the command line using ant.
  * sublogical, Issue 58<br>
  * arun.nurak, Issue 56 and Issue 57<br>
  * param.cool2008, Issue 55<br>
  * Paul T Keyser, IBM Watson Research Center, Issue 61<br>
  * Frederico Araujo, PhD student at the University of Texas at Dallas<br>
  * Ali Tozan<br>
  * Carl Anderson, Senior Scientist, ArchimedesModel.com<br>
  * Don Cochrane, from (?) University<br>
  * Mike Angelotti, from Miami University<br>
  * Chad Carff, University of Western Florida. EXCELLENT test cases, thank you<br>
  * Dr. Eman El-Sheikh, Ph.D., University of Western Florida<br>
  * Ravindra Guravannavar, Aztec Software, Bangalore<br>
  * Cameron Jenkins, University Of New Orleans<br>
  * Nils Knoblauch (Project Manager, Camline) - winner of the No Prize for the best bug report! Thanks!<br>
  * Phil Snowberger, Artificial Intelligence and Robotics Laboratory, University of Notre Dame<br>
  
= Change History (Update in reverse chronological order) =
1.6.0-Chp5-Rewrite : 08 Jan 2012 :<br>
Re-design and re-implementation of the adversarial search algorithms along with the addition of a Connect-4 GUI game
to demonstrate new capabilities. 
This is based on the following sub-project releases:
  * aima-core (0.10.4) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.7)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.2)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License. 
    
1.5.3-Chp17n21-Rewrite-DF1 : 16 Sept 2011 :<br>
Rewrite of logic from Chapter 17 and 21 in aima-core, defect fix release 1. 
This is based on the following sub-project releases:
  * aima-core (0.10.3) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.6)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.5.2-Chp17n21-Rewrite : 16 Sept 2011 :<br>
Rewrite of logic from Chapter 17 and 21 in aima-core. 
This is based on the following sub-project releases:
  * aima-core (0.10.2) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.6)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License. 
    
1.5.1-Chp15-Rewrite : 31 Jul 2011 :<br>
Rewrite of logic from Chapter 15 in aima-core. 
This is based on the following sub-project releases:
  * aima-core (0.10.1) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.5)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.5.0-Chp13-and-14-Rewrite : 03 Jul 2011 :<br>
Rewrite of logic from Chapters 13 and 14 in aima-core. 
This is based on the following sub-project releases:
  * aima-core (0.10.0) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.4)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.4.1-Minor-Fixes : 20 Mar 2011 :<br>
Defect fixes for aima-core and aimax-osm. 
This is based on the following sub-project releases:
  * aima-core (0.9.14) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.3)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License. 
    
1.4.0-OSM-Redesign : 19 Dec 2010 :<br>
Redesign of AIMAX-OSM implementation. Fixes for failed compilations and tests on UBUNTU platform. 
This is based on the following sub-project releases:
  * aima-core (0.9.13) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.3)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (2.0.0)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.3.2-Online+CSP+GUI-Improvements : 05 Nov 2010 :<br>
Improvements to implementations of Online, CSP, and GUI components. 
This is based on the following sub-project releases:
  * aima-core (0.9.12) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.2)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (1.0.2)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.
    
1.3.1-CSP+PathCost-Fixes : 02 Oct 2010 :<br>
This is a minor bug fix release. 
This is based on the following sub-project releases:
  * aima-core (0.9.11) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (1.0.1)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.3.0-CSP+4GUIs : 22 Aug 2010 :<br>
This is a minor release that includes many improvements added to the AIMAX-OSM project. 
This is based on the following sub-project releases:
  * aima-core (0.9.10) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (1.0.0)  : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (1.0.0)  : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.2.2-AIMA3e AIMAX-OSM Many Improvements : 19 June 2010 :<br>
This is a minor release that includes many improvements added to the AIMAX-OSM project. 
This is based on the following sub-project releases:
  * aima-core (0.9.9) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.2.0) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (0.9.2) : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.2.1-AIMA3e GUI Enhancements : 15 Mar 2010 :<br>
This is a minor release that includes improvements to the GUI components. 
This is based on the following sub-project releases:
  * aima-core (0.9.9) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.2.0) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (0.9.1) : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.2.0-AIMA3e AIMAX-OSM Map Style Redesign : 02 Mar 2010 :<br>
This is a minor release that includes redesign and performance enhancements to the AIMAX-OSM package. 
This is based on the following sub-project releases:
  * aima-core (0.9.9) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.1.2) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (0.9.0) : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  
    
1.1.1-AIMA3e Open Street Map (OSM) minor fixes : 09 Feb 2010 :<br>
This is a minor patch release to 1.1.0, which includes fixes/cleanup related to the new aimax-osm project. 
This is based on the following sub-project releases:
  * aima-core (0.9.9) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.1.2) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (0.1.2) : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License. 
    
1.1.0-AIMA3e Open Street Map (OSM) extension project added : 06 Feb 2010 :<br>
This is the first release of AIMA3e-Java with a new extension (aimax) project - Open Street Map (OSM) library, which 
leverages functionality from the two core (aima-core and aima-core) AIMA3e-Java projects. 
This is based on the following sub-project releases:
  * aima-core (0.9.8) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.1.1) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.
  * aimax-osm (0.1.1) : see - [http://aima-java.googlecode.com/svn/trunk/aimax-osm/README.txt README.txt] for details.
    * Note: This project is under a different License.  

1.0.0-AIMA3e Published : 10 Dec 2009 :<br>
This is the first full release based on the 3rd edition of AIMA. This is based on the following sub-project releases:
  * aima-core (0.9.7) : see - [http://aima-java.googlecode.com/svn/trunk/aima-core/README.txt README.txt] for details. 
  * aima-gui  (0.1.0) : see - [http://aima-java.googlecode.com/svn/trunk/aima-gui/README.txt README.txt] for details.

= Final Thoughts =

If you need any help with the java code, do write to me at ctjoreilly@gmail.com.

I am happy to receive any mails/bug reports and generally respond within a day, 
unless I am traveling. The only mails I do NOT respond to are those asking me 
to do your homework! Don't even try ! :-) 

Bug Reports are greatly appreciated! 

When you send in a bug report please include:
 # what you did to see the bug
 # what you expected to see
 # what you actually saw.

Doing so gets you added to the "Bug Report - Acknowledgments" list :-), Happy Hacking! 