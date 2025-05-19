package com.example.ticketapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Reports extends AppCompatActivity {

    WebView webView;
    DatabaseHelper databaseHelper;
    String tableName;
    Button btnDownloadPdf;
    String reportHtml; // Store HTML content for PDF generation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        webView = findViewById(R.id.webView);
        btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        databaseHelper = new DatabaseHelper(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        tableName = getIntent().getStringExtra("TABLE_NAME");

        loadReport();

        // Download PDF when button is clicked
        btnDownloadPdf.setOnClickListener(v -> generatePdf());
    }

    private void loadReport() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; }")
                .append("h2 { text-align: center; font-size: 24px; font-weight: bold; text-transform: uppercase; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("th, td { padding: 8px; text-align: center; border: 1px solid #ddd; font-size: 14px; }") // ✅ Adjusted font size
                .append("th { background-color: #f4b41a; color: white; }")
                .append("</style></head><body>");

        html.append("<h2>").append(tableName.replace("_", " ")).append(" REPORT</h2>");
        html.append("<table><tr>");

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        if (cursor != null && cursor.getCount() > 0) {
            String[] columnNames = cursor.getColumnNames();

            for (String column : columnNames) {
                html.append("<th>").append(column.toUpperCase()).append("</th>");
            }
            html.append("</tr>");

            while (cursor.moveToNext()) {
                html.append("<tr>");
                for (String column : columnNames) {
                    html.append("<td>").append(cursor.getString(cursor.getColumnIndexOrThrow(column))).append("</td>");
                }
                html.append("</tr>");
            }
        } else {
            html.append("<tr><td colspan='100%' style='text-align: center;'>No data available</td></tr>");
        }

        if (cursor != null) cursor.close();

        html.append("</table></body></html>");

        reportHtml = html.toString(); // Store HTML content for PDF
        webView.loadDataWithBaseURL(null, reportHtml, "text/html", "UTF-8", null);
    }

    private void generatePdf() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(tableName + "_Report");

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4); // ✅ Ensures proper A4 formatting
        builder.setResolution(new PrintAttributes.Resolution("res1", "Resolution", 600, 800));
        builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS); // ✅ Uses full page

        PrintJob printJob = printManager.print(tableName + "_Report", printAdapter, builder.build());

        if (printJob.isCompleted()) {
            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(this, "PDF Generation Failed", Toast.LENGTH_LONG).show();
        }
    }
}
