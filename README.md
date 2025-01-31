# three-phase-calendar

> [!IMPORTANT]
> *One Calendar App to rule them all, One Calendar App to find them; One Calendar App to bring them all and in the darkness bind them.*

## Project overview

This is my final calendar project, i.e. I plan to continue working on it over the years and turn it into a calendar-journaling-note system that I would actually like to use.

I named it "Three Phase Calendar", because the foundational idea behind this project is that it it is supposed to be **modal** - i.e. the user has access to three modes or views, a calendar mode, a planner/tasklist mode, and a journal mode. The journal mode is supposed to be relatively freeform, whereas calendar mode - rigid and structured. The idea behind the "planner/tasklist" is something sorta inbetween, allowing the users to be able to view their plans in different formats or work with their preferred one on a given day.

## Milestones/feature ToDo list

### Java + postgres backend server 

#### CALENDARCOINS COLLECTED: 14📅🪙!!!

- [x] ~~Dockerize your app properly~~
- [x] ~~Also integrate Minikube (optional?)~~
- [x] ~~Basic HTTP server functionality.~~
- [x] ~~Basic internal TaskEvent data handling.~~
- [x] ~~Basic postgres database & connectivity setup.~~
- [x] ~~TaskEvent database-side implementation.~~
- [x] ~~HTTP query parameters/Filter implementation.~~
- [x] ~~GET TaskEvent by ID request.~~
- [x] ~~POST TaskEvent.~~
- [x] ~~DELETE TaskEvent.~~
- [ ] **PUT TaskEvent (difficulty? easy peasy).**
- [ ] **PATCH TaskEvent (difficulty? lemon squezy (not easy)).**
- [x] ~~Integrate event types (`static`, `history`, `routine`)~~
- [ ] **`created_at` & `updated_at` function triggers.**
- [ ] **Implement PeriodEvent & TextEvent tables.**
- [ ] Server-side database schema initialization and validation (possibly future migration features?).
- [ ] postgres role for division of database access permissions.
- [ ] Substitute `HttpServer` with `HttpsServer` (#5).
- [ ] User authentication with JWT, bcrypt password encryption and OAuth 2.0.
- [x] ~~https://github.com/PerfectMach1ne/three-phase-calendar/issues/4~~
- [ ] Basic user & "calendarspace" implementation (to separate events from each separate user & their calendar/journal/planner).
- [ ] ForeignKey relation + GET all user/calendar events.
- [ ] PeriodEvent business-logic-side implementation.
- [ ] PeriodEvent server-context-side implementation.
- [ ] TextEvent business-logic-side implementation.
- [ ] TextEvent server-context-side implementation.

### Tauri x Vue.js 3 desktop + mobile + web app

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

### C + ncurses CLI frontend

- [x] ~~Integrate ncurses and libcurl into the build.~~
- [x] ~~Implement day-of-the-week, monthday and hour label windows.~~
- [ ] Implement weekday windows.
- [ ] Implement Vim motions for navigating the calendar.
- [ ] Implement libcurl data fetching.
- [ ] Read only journal.

## Project members

**Chief Enterprise Officer**: deceased

**Chief Technology Officer**: me but teal

**Product Manager**: me but orange

**Frontend Software Engineer**: me but pink and purple

**Backend Software Engineer**: me but purple

**Site Reliability Engineer**: me but gray

**Quality Assurance Tester**: me but pink

**UI/UX Designer**: me but blue and pink

**Home Barista Maid**: me but blue

**Scrum Master**: deceased

**Agile Coach**: deceased

**Recruitment Manager**: we don't need this guy.
