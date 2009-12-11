[This file is intended to be read in an editor with line wrapping.]

Introduction
------------

This is a "timeline UI" prototype. Multiple types of time-series data, variously static/interactive, historical/future, etc. are displayed in a single view.

Each application has UI consisting of individual events (which application provides a given event is distinguished by the color of their timestamp) and optionally a menu.

The implemented applications are:

  - A "calendar": user-created/editable events at specified times, which are automatically saved to disk. Interface is by the menu bar to create events and right-clicking on events to edit or delete them. There are two calendars in the prototype; future work would be to add more.

  - "Memos": Text may be entered in field always at the current time; upon pressing Enter the text is recorded as an uneditable event at the current time.

  - "Clock": an event always at the current time whose label is just a dash.

  - Some random junk events to help exercise the time-series-layout code.

Bugs
----

- Multiple events trying to be at the "current time", i.e. the memo field and clock, tend to hop around each other as they update at different times. My plan to fix this would be to add special handling for "now events" to the timeline panel so that they do not have to be updated on an arbitrary schedule by their timelines -- and "now" is a significant time for the UI anyway, so this seems reasonable.

- No attempt is made to separate user interface code from data model code (model/view or model/view/controller) in this prototype.

Code style
----------

The following are deliberate choices.

- Many fields are initialized in their declarations rather than the constructor. I do this whenever the initial value of the field does not depend in any way on the conditions under which the object is created.


Original project proposal notes
-------------------------------

CI245 Final Project Proposal

I have had this experiment on my mind for some time, and since it is an essentially GUI project I figure it would fit in well.

Timeline-Based User Interface

The idea is to integrate:

- The conversational aspect of command-line based interfaces: a series of user actions and responses presented in time order, which remain on screen even once any interaction with an individual task/application is complete.

- Graphical user interface idioms: designation by point-and-click, interactively modifiable views.

- Time-ordered application content such as calendars, logs, and chat sessions.

---

The fundamental interface entity will be one or more _timeline windows_. Each such window displays a series of events in a grid view with time vertically and one or more data columns horizontally.

Each window may display events from one or more applications, and application-specific interaction controls (such as command lines or search fields).

Multiple timeline event sources may be merged into one window or split into multiple windows interactively (as in modern web browser tabs).

---

Obviously this could be an immense project, a complete desktop replacement. This will be built along 'research prototype' lines instead: the core functionality implemented (timelines), and a number of very sketchy sample applications (simple command line, simple calendar, mock IM app).