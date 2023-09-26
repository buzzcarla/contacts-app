# contacts-app

1. Create a new Android project (app module, Java)

### for repository and lib module
1. Create a new module named repository (Kotlin)
   - Create model (additional fields: email, selfie/image in Base64 String)
   - Setup classes needed by Room Database
2. Create a new module named lib (Kotlin)
   - Add dependency injection using Dagger2, create module and component
   - use Coroutine for Room database transactions
   - Create the lib class, later to be accessed by app module (lib user will not need any DI knowledge to use the lib)

### for app module
1. Start working on app module. Create three fragments (AllContacts, AddContact, ContactSummary) and setup Navigation Component and the ViewModel
2. AllContactsFragment:
   - Show all saved contacts in a RecyclerView using custom adapter and data binding
   - Handle actions (add, tapping on the contact, etc)
3. ContactSummaryFragment:
   - Display details of the selected contact w/ data binding.
   - Handle actions (tapping on favorites, edit, delete, etc)
4. AddEditContactFragment:
   - Display details if there is a selected contact, blank if add new contact and with data binding
   - Open Camera intent on camera/image view tap
   - Handle actions (cancel, back, save, etc)

### for encryption
1. Create Crypto class in lib module, use AES256. Encryption can be done by encrypting entire database or encrypting the data inserted into the database. Choosing to encrypt data only to avoid use of third-party libs.
2. Implement in such a way that encryption settings can be set by the lib user, if encryption is ON, insert/update/delete Contact object is intercepted (encrypted) before sending for Room to handle
3. Update Lib constructor and add DI for the encryption
4. Update app module to make use of the encryption
   - On first app launch, prompt user if data will be encrypted or not

### finally
1. Cleanup and refactor
2. Test and fix issues
3. Generate release build and setup in Google Play for internal testing
4. Tag and release
