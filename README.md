# three-phase-calendar

> [!IMPORTANT]
> *One Calendar App to rule them all, One Calendar App to find them; One Calendar App to bring them all and in the darkness bind them.*

## Project overview

This is my final calendar project, i.e. I plan to continue working on it over the years and turn it into a calendar-journaling-note system that I would actually like to use.

I named it "Three Phase Calendar", because the foundational idea behind this project is that it it is supposed to be **modal** - i.e. the user has access to three modes or views, a calendar mode, a planner/tasklist mode, and a journal mode. The journal mode is supposed to be relatively freeform, whereas calendar mode - rigid and structured. The idea behind the "planner/tasklist" is something sorta inbetween, allowing the users to view their plans in different formats or work with their preferred one on a given day.

## Milestones/feature ToDo list

#### CALENDARCOINS COLLECTED: 46📅🪙!!!

### Java + postgres backend server

- [x] ~~Dockerize your app properly~~
- [x] ~~Basic HTTP server functionality.~~
- [x] ~~Basic internal TaskEvent data handling.~~
- [x] ~~Basic postgres database & connectivity setup.~~
- [x] ~~TaskEvent database-side implementation.~~
- [x] ~~HTTP query parameters/Filter implementation.~~
- [x] ~~GET TaskEvent by ID request.~~
- [x] ~~POST TaskEvent.~~
- [x] ~~DELETE TaskEvent.~~
- [x] ~~PUT TaskEvent (difficulty? easy peasy).~~
- [ ] PATCH TaskEvent (difficulty? lemon squezy (not easy)).
- [x] ~~Integrate event types (`static`, `history`, `routine`)~~
- [x] ~~`created_at` & `updated_at` adder functions.~~
- [x] ~~`updated_at` triggers.~~
- [x] ~~Implement PeriodEvent & TextEvent tables.~~
- [x] ~~Refactor "route"s to be "router"s instead.~~
- [x] ~~Decouple database interfacing for routers from DBConnProvider (bridge or proxy of some kind)~~
- [x] ~~Another stupid rework of Task and Timeblock schema.~~
- [x] ~~GET PeriodEvent.~~
- [x] ~~POST PeriodEvent.~~
- [x] ~~PUT PeriodEvent.~~
- [x] ~~DELETE PeriodEvent.~~
- [ ] PUT TextEvent.
- [ ] Make the QueryParam "validator strings" actually do something.
- [x] ~~Basic user & "calendarspace" implementation (to separate events from each separate user & their calendar/journal/planner).~~
- [x] ~~ForeignKey relation~~
- [x] ~~GET /api/login?id={user_id} returning user's entire calendarspace.~~
- [x] ~~For the love of all that lives, breathes and hates, please do your JWT token verification properly now that it works.~~
- [ ] **GET TextEvent.**
- [ ] **POST TextEvent.**
- [ ] **DELETE TextEvent.**
- [ ] **PATCH TextEvent**
- [x] ~~Basic user login with no auth.~~
- [x] ~~User authentication with JWT, bcrypt password encryption and OAuth 2.0.~~
- [ ] openssl keygen bash script
- [x] ~~Server-side database schema initialization and validation (possibly future migration features?).~~
- [ ] postgres role for division of database access permissions.
- [-] ~~Substitute `HttpServer` with `HttpsServer` (#5).~~
- [x] ~~https://github.com/PerfectMach1ne/three-phase-calendar/issues/4~~

### Tauri x Vue.js 3 desktop + mobile + web app

- [x] ~~Basic calendar component structure (vCalendar dead project reimplementation).~~
- [x] ~~Independent web app build. (Makefile or git subsomething?)~~
- [x] ~~Refactor BigCalendar.~~
- [ ] (OPTIONAL) Move most of the complex date handling to Rust "middleend".
- [x] ~~Implement a dark/light mode toggle.~~
- [x] ~~Implement a basic "casing" for calendar mode.~~

- [x] ~~Make Rust "middleend" be able to talk to the appropriate backend server.~~
- [x] ~~Make Rust backend and Vue learn how to talk to each other about JWT tokens and user id handling in requests.~~
- [x] ~~Wire up all event creation, fetching and deletion methods~~
- [x] ~~Correct the wiring between calendarspace and actual events (and make API server verify tokens)~~
- [ ] **Make events renderable as they should be.**
- [ ] **Make rendered events be fully functional and additionally clickable.**
- [x] ~~Event popup with the editing and PUT/PATCH part.~~
- [ ] **Events don't collide with each other and extend on the right Vue div box when they last more than 1 day.**
- [x] ~~Implement a basic "casing" for planner mode.~~
- [x] ~~Implement a basic "casing" for journal mode.~~
- [ ] **Planner mode and Journal mode can into calendarspace.**
- [ ] **Journal prototype implenentation.**
- [ ] **Planner prototype implementation.**
- [ ] **Much to your horror, run `npm tauri run android dev` and figure out how to thus make it at least not immediately present itself as a nightmare that it will come out as with nothing currently done with phones in mind.**
- [ ] Refurbish calendar styling.
- [ ] A settings popup/page.
- [ ] Make the dark/light mode toggle alter styling.
- [x] ~~Event creation forms.~~
- [x] ~~User authentication GUI.~~

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
