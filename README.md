# contacts-app

1. Create a new Android project (app module, Java)

### backend
1. Create a new module named repository (Kotlin)
   - Create model
   - Setup classes needed by Room Database
2. Create a new module named lib (Kotlin)
   - Add dependency injection using Dagger2, create module and component
   - Create the lib class, later to be accessed by app module

### frontend
1. Start working on app module. Create three fragments (AllContacts, AddContact, ContactSummary) and setup Navigation Component
2. AllContactsFragment:
   - Show all saved contacts in a RecyclerView using custom adapter and data binding
   - Handle actions (add, tapping on the contact, etc)