An app written in java, to allow fellow poets across the world to connect with each other,
the app makes use of firebase
......
add a googleservices.json files in the app module of your app



# Migrate to android x if neededpublic class Home extends AppCompatActivity

```java

    implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseUser User;
    FirebaseAuth OAuth;

    //private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        }
```