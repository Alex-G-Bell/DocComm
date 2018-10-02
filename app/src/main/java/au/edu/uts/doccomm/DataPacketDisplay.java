package au.edu.uts.doccomm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DataPacketDisplay extends AppCompatActivity {

    TextView packetDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_packet_display);

        packetDisplay = findViewById(R.id.dataPacketTV);
    }
}
