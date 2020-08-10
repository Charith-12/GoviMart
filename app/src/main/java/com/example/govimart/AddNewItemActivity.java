package com.example.govimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNewItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    DatePickerDialog datePickerDialog;
    EditText dateFrom, dateTo, dateHarvested, quantity, expectedPrice, location, description;
    TextView chosenCategoryTv, pleaseWaitTv;
    Spinner gradeSp, districtSp;
    private EditText mEditTextPostTitle;

    //
    private Button mButtonChooseImage;
    private Button mButtonAdd;
    private TextView mTextViewRemovePhotoBtn;


    private ImageView mImageView;
    private Uri mImageUri;
    private String mImageDownloadUrl;

    private ProgressBar mProgressBar;
    private ProgressBar mProgressBar2;

    // Database
    private StorageReference mStorageRef;
    StorageReference uploadedFileReference;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    // Authentication
    private FirebaseAuth mAuth;



    String gradeSelected;
    String districtSelected;
    //
    private boolean isUploadTaskComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        Intent intent = getIntent();
        String selectedCategory = intent.getStringExtra("SELECTED_CATEGORY");

        chosenCategoryTv = (TextView) findViewById(R.id.tv_selected_category);
        pleaseWaitTv = (TextView) findViewById(R.id.tv_please_wait);
        mEditTextPostTitle = (EditText) findViewById(R.id.et_post_title);
        quantity = (EditText) findViewById(R.id.et_quantity);
        expectedPrice = (EditText) findViewById(R.id.et_price);
        location = (EditText) findViewById(R.id.et_location);
        description = (EditText) findViewById(R.id.et_post_description);

        dateFrom = (EditText) findViewById(R.id.et_avail_date_from);
        dateTo = (EditText) findViewById(R.id.et_avail_date_to);
        dateHarvested = (EditText) findViewById(R.id.et_harvested_date);

        gradeSp = (Spinner) findViewById(R.id.spi_grade);
        districtSp = (Spinner) findViewById(R.id.spi_district);

        mButtonChooseImage = (Button) findViewById(R.id.btn_choose_image);
        mButtonAdd = (Button) findViewById(R.id.btn_post_items);
        mTextViewRemovePhotoBtn = (TextView) findViewById(R.id.tv_remove_photo);


        mTextViewRemovePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUri = null;
                Picasso.get().load(mImageUri).into(mImageView);
                //mImageView.setImageURI(mImageUri);
                mTextViewRemovePhotoBtn.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);

            }
        });


        mImageView = (ImageView) findViewById(R.id.image_view); // Image preview
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar); // Photo Upload progress bar
        mProgressBar2 = (ProgressBar) findViewById(R.id.pb_add_items); // Details Upload progress bar


        // Database References
        mStorageRef = FirebaseStorage.getInstance().getReference("imagesOfPosts");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("postUploads");

        mAuth = FirebaseAuth.getInstance();

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTapped();
            }
        });


        //To display the selected category
        chosenCategoryTv.setText(selectedCategory);

        // Date picker for Availability, From
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooser(dateFrom);
                /*
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddNewItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String dateFrm = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                dateFrom.setText(dateFrm);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();*/
            }
        });

        // Date picker for Availability, To
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooser(dateTo);
                /*
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddNewItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String dteTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                dateTo.setText(dteTo);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();*/
            }
        });

        // Date picker for Date Harvested
        dateHarvested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooser(dateHarvested);
                /*
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddNewItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String dteTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                dateHarvested.setText(dteTo);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show(); */
            }
        });


        // Grade Spinner
        ArrayAdapter<String> gradeAdepter = new ArrayAdapter<String>(AddNewItemActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Grades));
        gradeAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSp.setAdapter(gradeAdepter);
        gradeSp.setOnItemSelectedListener(this);
        //

        // District Spinner
        ArrayAdapter<String> districtAdepter = new ArrayAdapter<String>(AddNewItemActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Districts));
        districtAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSp.setAdapter(districtAdepter);
        districtSp.setOnItemSelectedListener(this);
        //

    }

    //Spinner's methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()){
            case R.id.spi_grade:
                gradeSelected = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),gradeSelected,Toast.LENGTH_SHORT).show();
                break;
            case R.id.spi_district:
                districtSelected = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),districtSelected,Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    ///


    public void dateChooser(EditText mEditText) {

        final EditText et = mEditText;

        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(AddNewItemActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        et.setText(date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public boolean validateInputs() {

        boolean isError = false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String startingDate = dateFrom.getText().toString().trim();
        String endDate = dateTo.getText().toString().trim();
        String harvestedDate = dateHarvested.getText().toString().trim();
        String pTitle = mEditTextPostTitle.getText().toString().trim();
        String pQuantity = quantity.getText().toString().trim();
        String pPrice = expectedPrice.getText().toString().trim();
        String pLocation = location.getText().toString().trim();

        // are fields empty?
        if (startingDate.isEmpty()) {
            dateFrom.setError("Please enter date");
            dateFrom.requestFocus();
            isError = true;
        } else dateFrom.setError(null);

        if (endDate.isEmpty()) {
            dateTo.setError("Please enter date");
            dateTo.requestFocus();
            isError = true;
        } else dateTo.setError(null);

        if (harvestedDate.isEmpty()) {
            dateHarvested.setError("Please enter date");
            dateHarvested.requestFocus();
            isError = true;
        } else dateHarvested.setError(null);

        if (pTitle.isEmpty()) {
            mEditTextPostTitle.setError("Title can not be Empty");
            mEditTextPostTitle.requestFocus();
            isError = true;
        } else mEditTextPostTitle.setError(null);

        if (pQuantity.isEmpty()) {
            quantity.setError("Quantity can not be Empty");
            quantity.requestFocus();
            isError = true;
        } else quantity.setError(null);

        if (pPrice.isEmpty()) {
            expectedPrice.setError("Price can not be Empty");
            expectedPrice.requestFocus();
            isError = true;
        } else expectedPrice.setError(null);

        if (pLocation.isEmpty()) {
            location.setError("Location can not be Empty");
            location.requestFocus();
            isError = true;
        } else location.setError(null);


        if (!startingDate.isEmpty() && !endDate.isEmpty() && !harvestedDate.isEmpty()) {

            // getting dates & converting into "Date" data type
            Date strDate = null;
            try {
                strDate = sdf.parse(dateFrom.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date edDate = null;
            try {
                edDate = sdf.parse(dateTo.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date hvsDate = null;
            try {
                hvsDate = sdf.parse(dateHarvested.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //
            // Getting today's Date
            String date1 = sdf.format(new Date());

            Date todayDate = null;
            try {
                todayDate = sdf.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //


            // "to" Date has to be later than "from" date
            if (strDate.getTime() > edDate.getTime()) {
                Toast.makeText(AddNewItemActivity.this, "Ending date cannot be before Starting date", Toast.LENGTH_LONG).show();
                dateFrom.setError("Please select a date before the ending date");
                dateTo.setError("Please select a date after the starting date");
                isError = true;
                //dateFrom.requestFocus();
            } else {
                dateFrom.setError(null);
                dateTo.setError(null);
            }


            // Available from date should be today or a future date
            if (todayDate.getTime() > strDate.getTime()) {
                Toast.makeText(AddNewItemActivity.this, "Available date should not be a past date", Toast.LENGTH_LONG).show();
                dateFrom.setError("Please select a valid date");
                isError = true;
                //dateFrom.requestFocus();
            } else dateFrom.setError(null);


            // Harvested date should be a prior date to the "Available from" date
            if (hvsDate.getTime() > strDate.getTime()) {
                Toast.makeText(AddNewItemActivity.this, "Available date cannot be a date before Harvested date", Toast.LENGTH_LONG).show();
                dateFrom.setError("Please select a date after the harvested date");
                dateHarvested.setError("Please select a date before the available date");
                isError = true;
                //dateFrom.requestFocus();
            } else {
                dateFrom.setError(null);
                dateHarvested.setError(null);
            }

            //TODO: Harvested date can be future?

        }

        if (isError) {
            return false;
        } else return true;

    }

    public void onBackIconTapped(View view) {
        onBackPressed();
    }

    public void postTapped() {
        if (validateInputs()) {

            //uploadItemDetails();
            if (mUploadTask != null && mUploadTask.isInProgress()) {   // To handle multiple button taps during upload
                Toast.makeText(AddNewItemActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
                /*if (isUploadTaskComplete) {
                    uploadItemDetails();
                }*/
            }


        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();
            Picasso.get()
                    .load(mImageUri)
                    .resize(800, 500)
                    .centerInside()
                    .into(mImageView);
            //mImageView.setImageURI(mImageUri);
            mTextViewRemovePhotoBtn.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            //mEditTextPhotoPath.setText(mImageUri.toString());

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {

        mProgressBar2.setVisibility(View.VISIBLE);
        pleaseWaitTv.setVisibility(View.VISIBLE);
        mButtonAdd.setClickable(false);

        isUploadTaskComplete = false;
        // Uploading of the image
        if (mImageUri != null) {

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            uploadedFileReference = fileReference;

            Toast.makeText(AddNewItemActivity.this, "Image is uploading", Toast.LENGTH_SHORT).show();

            /**/
            // Works tho
            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        //Log.e(TAG, "then: " + downloadUri.toString());

                        //
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setProgress(100);
                            }
                        }, 500);


                        Toast.makeText(AddNewItemActivity.this, "Image Upload successful. Please wait", Toast.LENGTH_LONG).show();

                            /*
                            UploadNewItem upload = new UploadNewItem(mEditTextPostTitle.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

                             */


                        //mImageDownloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        //mImageDownloadUrl = uploadedFileReference.getDownloadUrl().toString();
                        //mImageDownloadUrl = uploadedFileReference.toString();
                        /**/        mImageDownloadUrl = downloadUri.toString();

                        Log.d("TAG", "Image Down url");
                        System.out.println("DownLurL" + mImageDownloadUrl);
                        isUploadTaskComplete = true;

                        //uploadItemDetails();
                        getOwnerInfo();
                        //

                        /**/


                        //UploadNewItem upload = new UploadNewItem(mEditTextPostTitle.getText().toString().trim(),
                        //        downloadUri.toString());

                        //mDatabaseRef.push().setValue(upload);
                        /**/       } else
                    {
                        Toast.makeText(AddNewItemActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        //

                        // Alert Dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                        builder.setTitle("Image Upload Failed")
                                .setMessage("Are you want to try again?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //postTapped();
                                        uploadFile();
                                            /*if (isUploadTaskComplete) {
                                                uploadItemDetails();
                                            }*/
                                        /**/                      }
                                })
                                .setNegativeButton("No", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //
                        //
                    }
                }
            });

            // Works tho over
            /**/

            // Original below this
            /*

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);


                            Toast.makeText(AddNewItemActivity.this, "Image Upload successful. Please wait", Toast.LENGTH_LONG).show();

                            /*
                            UploadNewItem upload = new UploadNewItem(mEditTextPostTitle.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

                             */



            //mImageDownloadUrl = taskSnapshot.getUploadSessionUri().toString();
                     /*   //    mImageDownloadUrl = uploadedFileReference.getDownloadUrl().toString();
                            //mImageDownloadUrl = uploadedFileReference.toString();

                            Uri downloadUri = fileReference.getDownloadUrl().getResult();
                            mImageDownloadUrl = downloadUri.toString();

                            Log.d("TAG", "Image Down url");
                            System.out.println("DownLurL" + mImageDownloadUrl);
                            isUploadTaskComplete = true;

                            //uploadItemDetails();
                            getOwnerInfo();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            // Alert Dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                            builder.setTitle("Image Upload Failed")
                                    .setMessage("Are you want to try again?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //postTapped();
                                            uploadFile();
                                            /*if (isUploadTaskComplete) {
                                                uploadItemDetails();
                                            }*/
                      /*                  }
                                    })
                                    .setNegativeButton("No", null);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            //

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });

            /**/

            // Original over

        } else {
            Toast.makeText(this, "No image file selected. Only other details will add", Toast.LENGTH_SHORT).show();
            isUploadTaskComplete = true;
            //uploadItemDetails();
            getOwnerInfo();
        }


        ///////********************//////////////////////*****************////////////

        /*
        if (mImageUri != null)
        {
            mStorageRef.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return mStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        //Log.e(TAG, "then: " + downloadUri.toString());


                        UploadNewItem upload = new UploadNewItem(mEditTextPostTitle.getText().toString().trim(),
                                downloadUri.toString());

                        mDatabaseRef.push().setValue(upload);
                    } else
                    {
                        Toast.makeText(AddNewItemActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        */


        //////********************//////////////////////*****************/////////////

        /*
        //


        //progressDialog.setTitle("Image is Uploading...");
        //progressDialog.show();
        StorageReference storageReference2 = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

        storageReference2.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        //
                        String TempImageName = txtdata.getText().toString().trim();
                        //progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        @SuppressWarnings("VisibleForTests")
                        uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                        String ImageUploadId = databaseReference.push().getKey();
                        databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        //
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        // */


    }  // method close

    public void uploadItemDetails(String firstName, String mobileNo) {

        //

        final String postCategory = chosenCategoryTv.getText().toString();
        final String postTitle = mEditTextPostTitle.getText().toString();
        final double postQuantity = Double.parseDouble(quantity.getText().toString().trim());
        final String postGrade = gradeSelected.toString();
        final String postPrice = expectedPrice.getText().toString().trim();
        //final double postPrice = Double.parseDouble(expectedPrice.getText().toString().trim());
        final String postAvailableDateFrom = dateFrom.getText().toString();
        final String postAvailableDateTo = dateTo.getText().toString();
        final String postHarvestedDate = dateHarvested.getText().toString();
        final String postLocation = location.getText().toString();
        final String postDistrict = districtSelected.toString();
        final String postDescription = description.getText().toString();
        final String postImageUrl = mImageDownloadUrl;
        /*if (mImageDownloadUrl.isEmpty()) {
            postImageUrl = "https://firebasestorage.googleapis.com/v0/b/govi-mart-app.appspot.com/o/no-image.jpg?alt=media&token=f0e98b9d-39c2-456f-b041-70dab515024f";
        } else postImageUrl = mImageDownloadUrl;*/
        final String ownerId = mAuth.getCurrentUser().getUid();
        final String ownerFirstName = firstName;
        final String ownerMobileNo = mobileNo;




        // Database entry
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> post = new HashMap<>();
        post.put("pCategory", postCategory);
        post.put("pTitle", postTitle);
        post.put("pQuantity", postQuantity);
        post.put("pGrade", postGrade);
        post.put("pPrice", postPrice);
        post.put("pAvailableDateFrom", postAvailableDateFrom);
        post.put("pAvailableDateTo", postAvailableDateTo);
        post.put("pHarvestedDate", postHarvestedDate);
        post.put("pLocation", postLocation);
        post.put("pDistrict", postDistrict);
        post.put("pDescription", postDescription);
        post.put("pImageUrl", postImageUrl);
        post.put("pDateAdded", new Timestamp(new Date()));
        post.put("pOwnerId", ownerId);
        post.put("pOwnerName", ownerFirstName);
        post.put("pOwnerMobileNo", ownerMobileNo);

        //

        //


        db.collection("Posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Details upload success");
                        Toast.makeText(AddNewItemActivity.this, "Item Added Successfully", Toast.LENGTH_LONG).show();

                        mProgressBar2.setVisibility(View.GONE);
                        pleaseWaitTv.setVisibility(View.GONE);
                        mButtonAdd.setClickable(true);

                        // Alert Dialog on success
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                        builder.setTitle("Item Added!")
                                .setMessage("Press OK to go back home")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                });


                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Details upload Failed");
                        Toast.makeText(AddNewItemActivity.this, "Item Adding Failed!", Toast.LENGTH_LONG).show();

                        mProgressBar2.setVisibility(View.GONE);
                        pleaseWaitTv.setVisibility(View.GONE);
                        mButtonAdd.setClickable(true);

                        // Removing of uploaded image from the database
                        //

                        uploadedFileReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully

                                // Alert Dialog on failure; but file deleted
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                                builder.setTitle("Item Adding Failed")
                                        .setMessage("Press OK to go back or to retry")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        });


                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                //

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                int errorCode = ((StorageException) exception).getErrorCode();
                                String errorMessage = exception.getMessage();
                                Toast.makeText(AddNewItemActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                                // Alert Dialog on failure; image deletion also failed (redundant image on database)
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                                builder.setTitle("Item Adding Failed.Image Deletion Failed")
                                        .setMessage("Press OK to go back or to retry")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        });


                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                //

                            }
                        });
                        //



                    }
                });


    }

    public void getOwnerInfo(){

        //mProgressBar2.setVisibility(View.VISIBLE);
        //pleaseWaitTv.setVisibility(View.VISIBLE);
        //mButtonAdd.setClickable(false);

        // Authentication
        String ownerId = mAuth.getCurrentUser().getUid();
        // Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String[] mFirstName = new String[1];
        final String[] mMobileNo = new String[1];
        DocumentReference ownerDocRef = db.collection("Users").document(ownerId);
        ownerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        mFirstName[0] = (String) document.get("FirstName");
                        mMobileNo[0] = (String) document.get("MobileNo");

                        uploadItemDetails(mFirstName[0], mMobileNo[0]);


                    } else {
                        // Log.d(TAG, "No such document");
                        mFirstName[0] = "User Not Found";
                        mMobileNo[0] = "Not Found";

                        uploadItemDetails(mFirstName[0], mMobileNo[0]);
                    }
                } else {
                    // Log.d(TAG, "get failed with ", task.getException());
                    mFirstName[0] = "Error getting Username";
                    mMobileNo[0] = "Error getting number";

                    uploadItemDetails(mFirstName[0], mMobileNo[0]);
                }
            }
        });

    }


}