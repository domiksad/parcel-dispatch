# Models

___

## Human interface
- String getFullName()

## Person implements Human
- String fullName
- String address

## Courier implements Human
- Long id
- String fullName
- int parcelLimitPerDay
- Parcel[] currentParcels
- deliverParcels()
___

## Priority 
enum priority { LOW, HIGH }

## Status 
enum status { ACCEPTED, DISPATCHED, DELIVERED, CANCELED }
___

## Parcel 
- UUID id
- Person sender
- Person receiver 
- String deliveryCity;
- Int weight
- Priority priority
- Status status

___

## SortingRoom
- dictionary Parcel parcels
- assignParcel()

---

## ActionType
- enum ActionType { parcelTransfer (moving parcel from or to sorting room), parcelInfoUpdate }

## Action
- Long id
- ActionType actionType
- Timestamp time

## ParcelAction extends Action
- Parcel id

## History
- hashmap Parcel globalParcelList
- listarray Action 