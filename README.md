# three-phase-calendar

> [!IMPORTANT]
> *One Calendar App to rule them all, One Calendar App to find them; One Calendar App to bring them all and in the darkness bind them.*

## Project overview

This is my final calendar project, i.e. I plan to continue working on it over the years and turn it into a calendar-journaling-note system that I would actually like to use.

I named it "Three Phase Calendar", because the foundational idea behind this project is that it it is supposed to be **modal** - i.e. the user has access to three modes or views, a calendar mode, a planner/tasklist mode, and a journal mode. The journal mode is supposed to be relatively freeform, whereas calendar mode - rigid and structured. The idea behind the "planner/tasklist" is something sorta inbetween, allowing the users to be able to view their plans in different formats or work with their preferred one on a given day.

## Milestones/feature ToDo list

### Java + postgres backend server 

#### CALENDARCOINS COLLECTED: 8📅🪙!!!

- [x] Basic HTTP server functionality.
- [x] Basic internal TaskEvent data handling.
- [x] Basic postgres database & connectivity setup.
- [x] TaskEvent database-side implementation.
- [x] HTTP query parameters/Filter implementation.
- [x] GET TaskEvent by ID request.
- [ ] Basic user implementation (to separate events from each separate user & their calendar/journal/planner).
- [ ] ForeignKey relation + GET all user/calendar events.
- [ ] DELETE TaskEvent.
- [ ] PUT TaskEvent.
- [ ] PeriodEvent database-side implementation.
- [ ] PeriodEvent business-logic-side implementation.
- [ ] PeriodEvent server-context-side implementation.
- [ ] TextEvent database-side implementation.
- [ ] TextEvent business-logic-side implementation.
- [ ] TextEvent server-context-side implementation.
- [ ] Server-side database schema initialization and validation (possibly future migration features?).
- [ ] postgres role for division of database access permissions. 
- [ ] User authentication with JWT and OAuth 2.0.

### JavaScript + Node.js frontend server

- [ ] Basic HTTP server functionality.
- [ ] Basic calendar component structure (vCalendar dead project reimplementation).
- [ ] Basic Fetch API integration & API server connection.
- [ ] Basic event fetching.
- [ ] Calendar styling.
- [ ] Event object positioning & styling.
- [ ] Event creation forms.
- [ ] Journal prototype implenentation.
- [ ] Planner prototype implementation.
- [ ] User authentication GUI.

### Kotlin mobile app

- [ ] Basic Android project & build setup.
- [ ] Basic Jetpack Compose project setup.
- [ ] Basic connectivity with the server.
- [ ] Basic calendar UI elements.

### C + ncurses CLI frontend

- [x] Integrate ncurses and libcurl into the build.
- [x] Implement day-of-the-week, monthday and hour label windows.
- [ ] Implement weekday windows.
- [ ] Implement Vim motions for navigating the calendar.
- [ ] Implement libcurl data fetching.
- [ ] Read only journal.

## Project members

**Chief Technology Officer**: me but teal

**Product Manager**: me but orange

**Frontend Software Engineer**: me but pink and purple

**Backend Software Engineer**: me but purple

**Quality Assurance Tester**: me but pink

**UI/UX Designer**: me but blue

**Home Barista Maid**: me but blue

**Recruitment Manager**: we don't need this guy.
