

---------------------------------------------------------------------
[SERF: Stanford Entity Resolution Framework](http://infolab.stanford.edu/serf)
---------------------------------------------------------------------
*A modified/updated branch of SERF (@trevorprater May 2015).*

Original work done by Stanford InfoLab (April 2006).


Introduction
---------------------------------------------------------------------

The SERF project develops a generic infrastructure for Entity
Resolution (ER). ER is the task of identifying and combining data
records that represent the same real-life entities (e.g., customers,
or products).

This release of the SERF software provides an implementation of the
R-Swoosh algorithm described in reference [1]. The algorithm takes as
input a dataset of records (in CSV) and a "MatcherMerger" class that
implements functions to match and merge pairs of records, and returns
a dataset of resolved records.

A sample dataset of product records, along with a simple MatcherMerger
implementation is provided as an example (in the /example
directory). Records are matched based on the similarity of their
titles (using a Jaro-Winkler distance) and prices (through a relative
difference).

The source code of the SERF package is included (in the /src
directory), and is released under the BSD licence (see LICENSE).


Requirements
----------------------------------------------------------------------

- JDK6+

- The MatcherMerger class of the example uses the CMU secondstring
  library (http://secondstring.sourceforge.net/) to measure the
  similarity of product titles. For convenience, a jar of the library
  is provided under /lib. The license of the secondstring package is
  reproduced in the secondstring.LICENSE file, under /libs


Customizing SERF 
-----------------------------------------------------------------------

Let's see now how to use to SERF package to run entity resolution, on
your own data, with your own functions to match and merge records.

- Input data should be of CSV format.

- A matcherMerger class should implement the simple
  serf.data.MatcherMerger interface, which is described in

  [SERF_INSTALL]/doc/javadoc/serf/data/MatcherMerger.html

Where [SERF_INSTALL] is the location of your serf installation.

- A configuration file similar to example.conf should be provided,
  that points to your input data file and MatcherMerger
  implementation, and specifies the desired location for the output
  file.

- The SERF package can then be run as:

  java -cp "[SERF_INSTALL]/serf.jar:[YOURCP]" serf.ER your.conf

Where [YOURCP] represents a user defined classpath.

[YOURCP] should include the path to your MatcherMerger implementation
and any libraries it relies on.


Compiling the source code
-----------------------------------------------------------------------

From the [SERF_INSTALL] directory, create a "classes" directory:

    cd [SERF_INSTALL]
    mkdir classes
    
 Go to the src directory:
 
     cd src
    
 Then compile the SERF code:

    javac -cp ../libs  -d ../classes @sources
    

IntelliJ IDEA
-----------------------------------------------------------------------
This project can also be imported as a Gradle build or directly into
IntelliJ. It was rebuilt/repackaged in IntelliJ IDEA 14 CE.


References
-----------------------------------------------------------------------

[1] Swoosh: A Generic Approach to Entity Resolution
    Omar Benjelloun, Hector Garcia-Molina, Jeff Jonas, Qi Su, Jennifer
    Widom. Stanford University Technical Report, 2005.
    Available at: http://dbpubs.stanford.edu:8090/pub/2005-5

   
