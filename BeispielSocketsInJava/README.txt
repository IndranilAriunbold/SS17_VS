Contents

You should have received an archive containing this file (README) and
a directory called "examples" containing five Java files:
            EchoService.java
            MultithreadedTCPServer.java
            TCPClient.java
            TCPServer.java
            UserInterface.java


Compiling

You should be able to compile all five classes as usual:

    > cd examples
    > javac *.java


(You should compile all the files at once, as shown above).

You can (of course) import the files into a project in your IDE, but please
remember that you will need to be able to run the programs OUTSIDE the IDE.

Operation

Do not run the classes from inside the example directory, but from the parent
directory.  Start the server first (the client will crash if the server is not
running).

    > cd ..
    > java examples/TCPServer

The server will give no output until a client request is received.

In a seperate window, run the client

   >java examples/TCPClient
   Enter message for the Server, or end the session with . : this is a test
   Server answers: THIS IS A TEST
   Enter message for the Server, or end the session with . : Foo FBar
   Server answers: FOO BAR
   Enter message for the Server, or end the session with . : Enough.
   Server answers: ENOUGH.
   Enter message for the Server, or end the session with . : .
   >

Go back to the window with the server.  In the meantime, the server should have
given output like this:
   Received: this is a test
   Received: Foo Bar
   Received: Enough.
   Received: .
   Session ended, Server remains active

You should probably kill the server now.
(Use Ctrl-C in Linux or MacOS or whatever key works in your OS. ;-)

Now try the server with TWO clients - you should note that the first client
blocks the server for the duration of a session.  The multithreaded client
shows how a non-blocking (threaded) server works.


For more information contact

Prof. Ronald Moore

h_da
Hochschule Darmstadt
University of Applied Sciences

fbi
Fachbereich Informatik
http://www.fbi.h-da.de/organisation/personen/moore-ronald.html

