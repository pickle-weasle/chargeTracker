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

# Notes
- hardcode station charging speeds
- No auth or admin required, so single user
- Costs should accurately reflect time spent peak/offpeak
- 
### Inferred notes
- station ***speeds*** mentioned so presumably stations have different charging speeds
- price per unit appears to be uniform across all stations according to spec 
  - "Rates and station charging speeds can be..." and not "Station rates and charging speeds can be..."
  - no mention of EV charge curve, max rate, current battery etc so use station flat rate
  - assume tax included
- no input from actual EV battery or charging station - relies on user to stop and start accurately
- Single user, no auth, seeded data, will use embedded H2 file backed for simplicity & resilience
- including data to record price and charging speed in session data, not required by spec but straightforward to add from start
  - This doesn't however allow for rate & speed changing during charge, price and charge rates are set when charging begins tracking


## Features
- [ ] list of stations
- [ ] start/stop session
- [ ] charge calculation

## Potential features
- [ ] potentially 3 views, station list, current session, session history
- [ ] dateTime picker for finetuning/adjusting charge start/stop times


## Considerations
- start-end spanning multiple bands
- start-end spanning multiple days
- start-end crossing clock moving forward/backward - use duration
- server goes down/ user closes window and reopens
- start session only available if no sessions in progress already


## Scaffolding
 - using giter8 for barebones sbt project
   - `sbt new playframework/play-scala-seed.g8`
 - using angular for barebones for frontend
   - `ng new frontend --routing=false --style=css --ssr=false --skip-git`

