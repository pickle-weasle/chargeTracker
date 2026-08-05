# A note on scope
The features, notes, considerations below were initially written before I had received word on what the time frame would be. I began spec-ing out something considerably more complex given the allotted time.

~~As a result, this readme is no longer accurate to what I implemented. I will update it later this evening to reflect actual final code and what I was unable to cover/include.~~

I've made some change here to accurately reflect the state of the app and what I had hoped to do, given more time.

---

# Charge Tracker WIP
### An EV charging session tracker 

- shows list of charging stations
- user can start a charging session with a station
- when user is currently charging, they may choose to stop the charging session
- Upon session completion user is given energy usage and cost
- Pricing will take into account peak (8AM to 8PM) and off peak 

## To run the tracker
- from backend run `sbt run`
- from frontend run `ng serve`

## To run tests
- from backend run `sbt test`

# Notes
- hardcode station charging speeds
- No auth or admin required, so single user
- Costs should accurately reflect time spent peak/offpeak

### Inferred notes
- station ***speeds*** mentioned so presumably stations have different charging speeds
- price per unit appears to be uniform across all stations according to spec 
  - "Rates and station charging speeds can be..." and not "Station rates and charging speeds can be..."
  - no mention of EV charge curve, max rate, current battery etc so use station flat rate
  - assume tax included
- no input from actual EV battery or charging station - relies on user to stop and start accurately
- Single user, no auth, seeded data, ~~will use embedded H2 file backed for simplicity & resilience~~
  - Shorter than expected time frame meant H2 was removed in favor of using in memory TrieMap
- including data to record price and charging speed in session data, not required by spec but straightforward to add from start
  - This doesn't however allow for rate & speed changing during charge, price and charge rates are set when charging begins tracking
  - *This was to facilitate an accurate record of previous session pricing in the event of price rate, or charging speed changes*
 

## Completed Features
- [x] list of stations
- [x] start/stop session
- [x] charge calculation

## UNCOMPLETED Potential features
- [ ] potentially 3 views, station list, current session, session history (accessible via tabs)
  - final version has a single rudimentary view which encapsulates the station list, and details on most recent charge
  - a more feature rich station list would include
    - the use of angular material cards to contain station data and other material components for a modern look
    - name, address, charge speed, picture, number of times the user has charged here before 
  - a completed dedicated current session view would have been shown when the user starts tracking a charge and would have included 
    - details about current charging station
    - time elapsed since charge start
    - peak and off-peak units used along with current cost
  - a completed session history view would have shown the user previous charge sessions with accurate pricing.
    - user could sort by total units, off peak units, peak units, price
    - user could filter by specific stations

- [ ] dateTime picker for finetuning/adjusting charge start/stop times
  - a user of the app may forget to start/stop the charge tracker at the right time.
  - An improved system would prompt the user with a date picker loaded with the current instant when start or stop is initiated
  - This would allow the user to adjust start time (only allowing an instant in the past) and end time (only allowing an instant after the session start time)
  - This would also allow for easy testing of the system

## Features out of scope
- users location not taken into account, but a future improvement could order the stations by proximity
- users could use Google Maps integration to view stations close to them

## Considerations
- start-end spanning multiple bands
- start-end spanning multiple days
- start-end crossing clock moving forward/backward - use duration
  - NOT IMPLEMENTED - I thought this was going to be the most interesting aspect of the assignment but time didn't allow for it
- server goes down/ user closes window and reopens
  - PARTIALLY IMPLEMENTED - data loss at server stop would have been solved with persistent DB. Closing window and reopen *does* work
- start session only available if no sessions in progress already

---

## Scaffolding
 - using giter8 for barebones sbt project
   - `sbt new playframework/play-scala-seed.g8`
 - using angular for barebones for frontend
   - `ng new frontend --routing=false --style=css --ssr=false --skip-git`

## Station & Session data
- station data is hardcoded as a scala list
- session data is stored in in-memory TrieMap
- final version includes no persistent DB, this would be the first thing I'd upgrade. 
  - When server goes down, current & previous charge sessions are lost, *persistent DB would allow resuming session after server restart*
  - *persistent DB would allow the user to view previous sessions, filter by station, order by total cost etc*