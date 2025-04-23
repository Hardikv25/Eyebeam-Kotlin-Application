package com.example.Eyebeam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddressDetails : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var contactNameEditText: EditText
    private lateinit var contactPhoneEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var pincodeEditText: EditText
    private lateinit var stateSpinner: Spinner
    private lateinit var countrySpinner: Spinner
    private lateinit var saveAddressButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_details)

        sharedPreferences = getSharedPreferences("OrderDetails", MODE_PRIVATE)
        setupViews()
        setupCountrySpinner()
        setupStateSpinner()
    }
    private fun setupViews() {

        // Initialize Views
        contactNameEditText = findViewById(R.id.et_contact_name)
        contactPhoneEditText = findViewById(R.id.et_contact_phone)
        streetEditText = findViewById(R.id.et_street)
        citySpinner = findViewById(R.id.spinner_city)
        pincodeEditText = findViewById(R.id.et_pincode)
        stateSpinner = findViewById(R.id.spinner_state)
        countrySpinner = findViewById(R.id.spinner_country)
        saveAddressButton = findViewById(R.id.btn_save_address)

        // Back Arrow Listener
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish() // Close the activity when back is pressed
        }
        saveAddressButton.setOnClickListener {
            saveAddress()
        }
    }
    private fun setupCountrySpinner() {

        // Set up Country Spinner
        val countries = arrayOf(
            "India",
            "United States",
            "Canada",
            "Australia",
            "United Kingdom",
            "Germany",
            "France",
            "Japan",
            "China",
            "Brazil"
        )
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = countryAdapter
    }
    private fun setupStateSpinner() {

        // State and City Map
        val stateCityMap = mapOf(
            "Andhra Pradesh" to listOf("Visakhapatnam", "Vijayawada", "Guntur", "Tirupati"),
            "Arunachal Pradesh" to listOf("Itanagar", "Tawang", "Ziro", "Pasighat"),
            "Assam" to listOf("Guwahati", "Dibrugarh", "Jorhat", "Silchar"),
            "Bihar" to listOf("Patna", "Gaya", "Bhagalpur", "Muzaffarpur"),
            "Chhattisgarh" to listOf("Raipur", "Bilaspur", "Korba", "Durg"),
            "Goa" to listOf("Panaji", "Vasco da Gama", "Margao", "Mapusa"),
            "Gujarat" to listOf(
                "Surat",
                "Ahmedabad",
                "Vadodara",
                "Rajkot",
                "Gandhinagar",
                "Bhavnagar",
                "Jamnagar",
                "Junagadh",
                "Anand",
                "Nadiad",
                "Kheda",
                "Mehsana",
                "Palanpur",
                "Porbandar",
                "Gandhidham",
                "Vapi",
                "Dahod",
                "Navsari",
                "Surendranagar",
                "Bharuch",
                "Diu",
                "Banas Kantha",
                "Mahisagar",
                "Bhuj",
                "Sihor",
                "Modasa",
                "Godhra",
                "Saputara",
                "Valsad",
                "Lunawada",
                "Dharampur",
                "Kutch",
                "Vijapur",
                "Kapadvanj",
                "Chhota Udaipur",
                "Narmada",
                "Upleta"
            ),
            "Haryana" to listOf("Gurgaon", "Faridabad", "Panipat", "Ambala"),
            "Himachal Pradesh" to listOf("Shimla", "Manali", "Dharamshala", "Solan"),
            "Jharkhand" to listOf("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro"),
            "Karnataka" to listOf("Bengaluru", "Mysuru", "Mangaluru", "Hubli"),
            "Kerala" to listOf("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur"),
            "Madhya Pradesh" to listOf("Bhopal", "Indore", "Gwalior", "Jabalpur"),
            "Maharashtra" to listOf("Mumbai", "Pune", "Nagpur", "Nashik"),
            "Manipur" to listOf("Imphal", "Thoubal", "Bishnupur", "Churachandpur"),
            "Meghalaya" to listOf("Shillong", "Tura", "Nongpoh", "Cherrapunji"),
            "Mizoram" to listOf("Aizawl", "Lunglei", "Champhai", "Serchhip"),
            "Nagaland" to listOf("Kohima", "Dimapur", "Mokokchung", "Wokha"),
            "Odisha" to listOf("Bhubaneswar", "Cuttack", "Rourkela", "Puri"),
            "Punjab" to listOf("Amritsar", "Ludhiana", "Jalandhar", "Patiala"),
            "Rajasthan" to listOf("Jaipur", "Udaipur", "Jodhpur", "Kota"),
            "Sikkim" to listOf("Gangtok", "Namchi", "Mangan", "Gyalshing"),
            "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Salem"),
            "Telangana" to listOf("Hyderabad", "Warangal", "Nizamabad", "Karimnagar"),
            "Tripura" to listOf("Agartala", "Dharmanagar", "Udaipur", "Kailasahar"),
            "Uttar Pradesh" to listOf("Lucknow", "Kanpur", "Varanasi", "Agra"),
            "Uttarakhand" to listOf("Dehradun", "Haridwar", "Nainital", "Roorkee"),
            "West Bengal" to listOf("Kolkata", "Darjeeling", "Siliguri", "Howrah"),
            "Andaman and Nicobar Islands" to listOf("Port Blair", "Havelock Island", "Neil Island"),
            "Chandigarh" to listOf("Chandigarh"),
            "Dadra and Nagar Haveli and Daman and Diu" to listOf("Daman", "Silvassa", "Diu"),
            "Lakshadweep" to listOf("Kavaratti", "Minicoy"),
            "Delhi" to listOf("New Delhi", "Old Delhi"),
            "Puducherry" to listOf("Puducherry", "Karaikal", "Yanam", "Mahe")
        )

        // Set up State Spinner
        val stateAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, stateCityMap.keys.toList())
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner.adapter = stateAdapter

        // Update City Spinner based on State Selection
        stateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedState = stateSpinner.selectedItem.toString()
                updateCitySpinner(stateCityMap[selectedState] ?: emptyList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun updateCitySpinner(cities: List<String>) {
        // Populate City Spinner based on selected state
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter
    }

    private fun saveAddress() {
        val contactName = contactNameEditText.text.toString()
        val contactPhone = contactPhoneEditText.text.toString()
        val street = streetEditText.text.toString()
        val city = citySpinner.selectedItem.toString()
        val pincode = pincodeEditText.text.toString()
        val state = stateSpinner.selectedItem.toString()
        val country = countrySpinner.selectedItem.toString()
        val totalAmount = sharedPreferences.getString("TotalAmount", "0")
        // Regular expression for validating a 10-digit phone number
        val phonePattern = Regex("^\\d{10}$")
        // Regular expression for validating a 6-digit pincode
        val pincodePattern = Regex("^\\d{6}$")

        if (contactName.isEmpty() || contactPhone.isEmpty() || street.isEmpty() ||
            city.isEmpty() || pincode.isEmpty() || state.isEmpty()) {
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show()
        } else if (!phonePattern.matches(contactPhone)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
        } else if (!pincodePattern.matches(pincode)) {
            Toast.makeText(this, "Please enter a valid 6-digit pincode", Toast.LENGTH_SHORT).show()
        } else {

            sharedPreferences.edit().apply {
                putString("TotalAmount", totalAmount)
                putString("CustomerName", contactName)
                putString("CustomerPhone", contactPhone)
                putString("Street", street)
                putString("City", city)
                putString("Pincode", pincode)
                putString("State", state)
                putString("Country", country)
                apply()
            }
            Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show()
            // Proceed with saving the address and navigating to the next screen
            val intent = Intent(this@AddressDetails, payment::class.java)
            startActivity(intent)


        }
    }
}
