# IntelliJ RunConfig Loader
IntelliJ itself is the greatest IDE I've ever used. But one important feature, the IDE doesn't provide by itself: Import and export Run Configurations. And that's what this plugin is developed for.

## Import
There are two ways to import configurations into your project. 
### Import them manually
1. Click *Tools &rarr; RunConf Handler &rarr; Import Run Confgurations* and choose the folder which contains your configurations. 
2. You now see the new configurations which will be imported. Note that if an extension with the same name and the same type already exists, the extension will be skipped.
3. Press *Import*. 
4. The configurations are now imported and you can start using them.

### Import them at each start
1. Open the Settings: *File &rarr; Settings &rarr; RunConf Handler*.
2. Now enable the checkbox and choose the path you'd like to load your configurations from, everytime the project is reopened.
3. The *new* configurations, will now be imported each time, the IDE starts.

## Export
To export all project run configurations, simply use the export functionality:
1. Click *Tools &rarr; RunConf Handler &rarr; Export Run Confgurations* and choose the folder where you'd like to export your configurations to.
2. In the list at the bottom of the dialog, you see all configurations which will be exported.
3. Press *Export*. 
