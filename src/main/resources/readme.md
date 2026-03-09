Commands:
```
help <command> - display help message

create <person | courier | parcel> *params* - create new <person | courier | parcel>
create person "full name" "address"
create courier "full name" <parcelLimitPerDay>
create parcel <index of a sender> <index of a receiver> "delivery city" <weight> priority<high | low>

list <person | courier | parcel | sortingRoom | all> - list all <person | courier | parcel | all> objects

process parcel <parcel index | parcel uuid> - moves parcel to sorting room
process sortingRoom <courier id | index> - gives parcel to courier
process courier <id> - process all packages that courier has
process courier <id> <parcel index | parcel uuid> - process one package that courier has

find <parcel uuid> - gives information about parcel

delete <index> - deletes unprocessed parcel by index

cancel <parcel uuid> - cancels parcel

stats - prints out stats"

log <print, dump> - displays or dumps logs
```