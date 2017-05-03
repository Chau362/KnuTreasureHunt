package pearsistent.knutreasurehunt;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class CreatePDF extends AppCompatActivity {

    private OutputStream output;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private File pdfFolder;
    private File myFile;

    private String imagePath;
    private ArrayList<Team> teamList = null;
    private String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");

        // Read from the database
        mDatabase.child("Team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ///팀을 받아와야 한다..
                teamList = new ArrayList<Team>();
                Log.d("DB", "Cheer up ");

                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    Team currentTeam = teamSnapshot.getValue(Team.class);
                    teamList.add(currentTeam);
                    Log.d("TeamList", "" + currentTeam.getTeamName());
                }
                ArrayList<Document> docList = new ArrayList<Document>();
                //You should create pdf in listener
                try {
                    //Create pdf and upload for each team
                    for (int i = 0; i < teamList.size(); i++) {
                        //add data in here
                        //Step 1
                        Document document = new Document();
                        docList.add(document);
                        //Step 2
                        PdfWriter.getInstance(docList.get(i), output);
                        //Step 3
                        docList.get(i).open();
                        //Step 4 add data to document
                        PdfPTable table = new PdfPTable(2);
                        //Convert int to string
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(teamList.get(i).getTeamPoint());
                        String point = sb.toString();

                        table.addCell(teamList.get(i).getTeamName());
                        table.addCell("score : "+point);
                        //point, 멤버 더하기
                        docList.get(i).add(table);
//                      document.add(new Paragraph("5.3"));
//                      document.add(new Paragraph("good day"));


                        //////Upload File to Firebase
                        String name = teamList.get(i).getTeamName();
                        StorageReference ref = storageRef.child(name + "/" + name + ".pdf");
                        uploadFile(ref);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }finally {
                    //Step 5: Close the document
                    for(int i=0; i<teamList.size(); i++)
                       docList.get(i).close();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });


        Log.d("CreatePDF", "open success");
        try {
            createPath();
            //  uploadFile();
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            //viewPdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createPath() throws FileNotFoundException {
        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, "Doc1.pdf");
        output = new FileOutputStream(myFile);
    }

    // @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createPDF(int count) throws DocumentException, IOException {
        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 add data to document
        PdfPTable table = createTable(teamList.get(count));

        document.add(table);

        document.add(new Paragraph("5.3"));
        document.add(new Paragraph("good day"));

        //Step 5: Close the document
        document.close();
    }

    private PdfPTable createTable(Team team) {
        PdfPTable table = new PdfPTable(8);
        for (int i = 0; i < 3; i++) {
            table.addCell(team.getTeamName());
            int point = team.getTeamPoint();
            //  table.addCell(point);
        }
        return table;
    }

    //this version is static. so you have to change it to work dynamically
    private void uploadFile(StorageReference childRef) {
        //여기에 ref를 팀마다 받아오고
        //팀 리스트 사이즈만큼 pdf 생성
        //final StorageReference childRef = storageRef.child("bogyuteam/Test.pdf");
        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", myFile);
        UploadTask uploadTask = childRef.putFile(objectURI);
        childRef.putFile(objectURI);
        Log.d("Upload pdf", "success");

    }

    private StorageReference findImageFile(String imageFileName) {

        if (teamName != null) {
            //can make a image file name
            StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);
            return childRef;
        }

        return null;
    }
}