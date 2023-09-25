# contacts-app

1. Create a new Android project (app module, Java)

### backend
1. Create a new module named repository (Kotlin)
   - Create model (additional fields: email, selfie/image)
   - Setup classes needed by Room Database
2. Create a new module named lib (Kotlin)
   - Add dependency injection using Dagger2, create module and component
   - use Coroutine for Room database transactions
   - Create the lib class, later to be accessed by app module (lib user will not need any DI knowledge to use the lib)

### frontend
1. Start working on app module. Create three fragments (AllContacts, AddContact, ContactSummary) and setup Navigation Component
2. AllContactsFragment:
   - Show all saved contacts in a RecyclerView using custom adapter and data binding
   - Handle actions (add, tapping on the contact, etc)
3. ContactSummaryFragment:
   - Display details of the selected contact w/ data binding.
   - Handle actions (tapping on favorites, edit, delete, etc)
4. AddEditContactFragment:
   - Display details if there is a selected contact, blank if add new contact w/ data binding
   - Open Camera intent on camera/image view tap
   - Handle actions (cancel, back, save, etc)
