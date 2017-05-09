package pearsistent.knutreasurehunt;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreatePDF extends AppCompatActivity {

    private OutputStream output;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private File pdfFolder;
    private File myFile;
    private ArrayList<File> fileList;
    private ArrayList<OutputStream> outputStreamArrayList;
    private int teamNum = 0;
    private int count = 0;
    final ArrayList<Uri> uris = new ArrayList<>();
    private int flag = 0;

    private EditText email;
    private Button send;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://knutreasurehunt.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://knutreasurehunt.appspot.com");
        fileList = new ArrayList<File>();
        outputStreamArrayList = new ArrayList<>();

        //////Edited by bogyu , david you can add function that send email in here
        email = (EditText) findViewById(R.id.email);
        send = (Button) findViewById(R.id.send);


        // Read from the database
        mDatabase.child("Team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ///팀을 받아와야 한다..

                Log.d("DB", "Cheer up ");

                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    Team currentTeam = teamSnapshot.getValue(Team.class);

                    //current team itemlist setting
                    for (int i = 0; i < currentTeam.getItemList().size(); i++) {

                        Log.i("Who!!", currentTeam.getTeamName());

                        if (currentTeam.getItemList().get(0).getName() != "null")
                            currentTeam.getItemList().get(i).setImageReference(findImageFile(currentTeam.getItemList().get(i).getName() + ".jpg", currentTeam.getTeamName()));
                    }

                    //DownloadStorage(currentTeam);

                    createPDFPath(currentTeam.getTeamName());
                    makePDF(currentTeam, teamNum);


                    teamNum++;
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == teamNum){
                    flag = 0;
                    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    emailIntent.setType("application/pdf");
                //emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"seul0411@naver.com"});
                    emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.getText().toString()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PDF with the TreasureHunt summary");
                    emailIntent.putExtra(Intent.EXTRA_TEXT   , "Here is attached the PDF with the TreasureHunt summary");


                    for(File f :pdfFolder.listFiles()){
                        Uri uri = Uri.fromFile(f);
                        uris.add(uri);
                    }

                    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
                //emailIntent.put
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(CreatePDF.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else{
                        Toast.makeText(CreatePDF.this, "Creating PDF file... wait please...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void makePDF(final Team currentTeam, final int i) {


        Log.i("MAKEPDF", currentTeam.getTeamName());
        final Document document = new Document();

        try {


            PdfWriter.getInstance(document, outputStreamArrayList.get(i));
            document.open();

            float fntSize, lineSpacing;

            //Title
            fntSize = 60.7f;
            lineSpacing = 300f;
            Paragraph title = new Paragraph(new Phrase(lineSpacing,"Treasure Hunt",
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
            title.setPaddingTop(40);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            //TeamName
            fntSize = 40.7f;
            lineSpacing = 300f;
            Paragraph teamName = new Paragraph(new Phrase(lineSpacing,currentTeam.getTeamName(),
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
            teamName.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(teamName);


            fntSize = 20.7f;
            lineSpacing = 21f;

            //TeamMember
            for(int j=0;j<currentTeam.getTeamMembers().size();j++){
                Paragraph teamMember =  new Paragraph(new Phrase(lineSpacing,currentTeam.getTeamMembers().get(j).getMemberName(),
                        FontFactory.getFont(FontFactory.COURIER, fntSize)));

                teamMember.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(teamMember);

            }

            /*//TeamPoint
            fntSize = 20.7f;
            lineSpacing = 20f;
            Paragraph teamPoint = new Paragraph(new Phrase(lineSpacing,"Team point : "+currentTeam.getTeamPoint(),
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
            teamPoint.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(teamPoint);*/

            //Date
            fntSize = 16.7f;
            lineSpacing = 23f;
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());

            Paragraph date = new Paragraph(new Phrase(lineSpacing, formattedDate,
                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
            date.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(date);

            //Add images
            addImageToPDF(currentTeam, document, i);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void addImageToPDF(final Team currentTeam, final Document document, final int index) {
        Log.i("AddImageToPDF", "INTO!");
        final ArrayList<File> FileList = new ArrayList<File>();
        int itemCount = 0;


        for (int item_count = 0; item_count < currentTeam.getItemList().size(); item_count++) {
            final File tempfile = getFile(count++);
            itemCount++;
            final int temp = itemCount;

            final StorageReference islandRef = currentTeam.getItemList().get(item_count).getImageReference();
            Log.d("ItemList", currentTeam.getTeamName() + "  " + currentTeam.getItemList().get(item_count).getName());

            if (currentTeam.getItemList().get(0).getName().equals("null")) {
                //Toast.makeText(CreatePDF.this, currentTeam.getTeamName() + " doesn't have items.", Toast.LENGTH_SHORT).show();
                document.close();
                flag++;
            } else {
                islandRef.getFile(tempfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        FileList.add(tempfile);
                        Log.d("makeFile", "Success");
                        Log.d("FileListSize", "" + FileList.size());
                        String path = tempfile.getPath();
                        Log.d("FilePath", "" + path);

                        try {
                            document.newPage();

                            float fntSize, lineSpacing;
                            Image image = Image.getInstance(tempfile.getPath());


                            if(image.getPlainHeight()/4 < document.getPageSize().getHeight()-200)
                                image.scaleAbsolute(image.getPlainWidth()/4,image.getPlainHeight()/4);
                            else if(image.getPlainHeight()/4 > document.getPageSize().getHeight()-200)
                                image.scaleAbsolute(image.getPlainWidth()/(float)4.5,image.getPlainHeight()/(float)4.5);

                            image.setAlignment(Image.ALIGN_CENTER);
                            document.add(image);

                            fntSize = 20.7f;
                            lineSpacing = 25f;
                            Paragraph itemTitle = new Paragraph(new Phrase(lineSpacing,currentTeam.getItemList().get(temp-1).getName(),
                                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
                            itemTitle.setPaddingTop(40);
                            itemTitle.setIndentationRight(30);
                            document.add(itemTitle);

                            fntSize = 13.7f;
                            lineSpacing = 23f;
                            Paragraph itemDescript = new Paragraph(new Phrase(lineSpacing,
                                    currentTeam.getItemList().get(temp-1).getText()+" ("+
                                            currentTeam.getItemList().get(temp-1).getPoints()+" pts)\n",
                                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
                            //itemTitle.setPaddingTop(40);
                            document.add(itemDescript);


                            Log.i("temp"," "+temp);
                            if (temp == currentTeam.getItemList().size()) {

                                document.close();
                                flag++;
                                Log.i("FLAGGGG"," "+flag);
                                //uploadPDFFile(currentTeam.getTeamName(), index);
                            }

                        } catch (IOException e) {
                            Log.i("Errrrrrr", "InputStream");
                            e.printStackTrace();
                        } catch (BadElementException e) {
                            e.printStackTrace();
                            Log.i("Errrrrrr", "Image");
                        } catch (DocumentException e) {
                            Log.i("Errrrrrr", "" + currentTeam.getTeamName());
                            Log.i("Errrrrrr", "Document");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        Log.i("AddImageToPDF", "FINISH!");

    }


    public void createPDFPath(String teamName) {
        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, teamName + ".pdf");
        try {
            output = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileList.add(myFile);
        outputStreamArrayList.add(output);

    }

    //Upload File to Firebase
    private void uploadPDFFile(String name, int i) {
        //여기에 ref를 팀마다 받아오고
        //팀 리스트 사이즈만큼 pdf 생성
        StorageReference childRef = storageRef.child(name + "/" + name + ".pdf");
        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileList.get(i));
        UploadTask uploadTask = childRef.putFile(objectURI);
        childRef.putFile(objectURI);
        Log.d("Upload pdf", "success");
    }

    private StorageReference findImageFile(String imageFileName, String teamName) {

        if (teamName != null) {
            //can make a image file name
            StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);
            return childRef;
        }

        return null;
    }

    private File getFile(int num) {

        File folder = new File("sdcard/pdf_images");

        if (!folder.exists()) {
            folder.mkdir();
        }

        //dynamically make a file name
        File imageFile = new File(folder, "Image" + num + ".jpg");

        if (imageFile.exists()) {
            imageFile.delete();
            imageFile = new File(folder, "Image" + num + ".jpg");
        }

        return imageFile;
    }


    private Uri getFileUri(File fileName) {

        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileName);

        return objectURI;
    }

}