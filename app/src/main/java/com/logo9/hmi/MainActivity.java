package com.logo9.hmi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.logo9.hmi.screens.IOActivity;

import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends Activity {
    private EditText deviceName;
    private EditText ip;
    private EditText modbusPort;
    private EditText webPort;
    private EditText unitId;
    private EditText address;
    private EditText quantity;
    private EditText value;
    private CheckBox https;

    private TextView statusPill;
    private TextView connectionSummary;
    private TextView overviewName;
    private TextView overviewIp;
    private TextView overviewModel;
    private TextView overviewModbus;
    private TextView overviewWeb;
    private TextView values;

    private final ModbusTcpClient modbus = new ModbusTcpClient();

    private final int bg = Color.parseColor("#07131F");
    private final int panel = Color.parseColor("#102333");
    private final int panelAlt = Color.parseColor("#0D1E2D");
    private final int cyan = Color.parseColor("#22D3EE");
    private final int cyanSoft = Color.parseColor("#16384A");
    private final int text = Color.parseColor("#F4F7FA");
    private final int muted = Color.parseColor("#9CB0C0");
    private final int success = Color.parseColor("#10B981");
    private final int successBg = Color.parseColor("#12392F");
    private final int warning = Color.parseColor("#F59E0B");
    private final int danger = Color.parseColor("#EF4444");
    private final int dangerBg = Color.parseColor("#3B1720");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        build();
        load();
        refreshOverview();
        autoDiagnose();
    }
    private void autoDiagnose() {

    new android.os.Handler().postDelayed(() -> {
        diagnose();
    }, 1500);

}
    private void build() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(bg);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(18), dp(18), dp(24));
        scrollView.addView(root, new LinearLayout.LayoutParams(-1, -2));

        root.addView(buildHeaderCard());
        root.addView(space(12));
        root.addView(buildOverviewCard());
        root.addView(space(12));
        root.addView(buildConnectionCard());
        root.addView(space(12));
        root.addView(buildReadCard());
        root.addView(space(12));
        root.addView(buildWriteCard());
        root.addView(space(12));
        root.addView(buildFooterCard());

        setContentView(scrollView);
    }

    private View buildHeaderCard() {
        LinearLayout card = card();
        card.addView(title("LOGO! HMI PRO", 28));
        TextView subtitle = body("Industrial Automation Controller");
        subtitle.setPadding(0, dp(6), 0, 0);
        card.addView(subtitle);

        LinearLayout badgeRow = new LinearLayout(this);
        badgeRow.setOrientation(LinearLayout.HORIZONTAL);
        badgeRow.setPadding(0, dp(14), 0, 0);
        badgeRow.addView(chip("Modbus TCP", cyan, cyanSoft));
        badgeRow.addView(chip("HMI Web", warning, Color.parseColor("#3B2B12")));
        badgeRow.addView(chip("Android 11+", text, panelAlt));
        card.addView(badgeRow);
        return card;
    }

    private View buildOverviewCard() {
        LinearLayout card = card();
        card.addView(section("Resumen del equipo"));

        statusPill = chip("Listo para conectar", muted, panelAlt);
        LinearLayout statusWrap = new LinearLayout(this);
        statusWrap.setPadding(0, dp(10), 0, 0);
        statusWrap.addView(statusPill);
        card.addView(statusWrap);

        connectionSummary = body("Aún no se ha realizado un diagnóstico de red.");
        connectionSummary.setPadding(0, dp(10), 0, 0);
        card.addView(connectionSummary);

        card.addView(space(10));
overviewName = metricLine(card, "Perfil", "PLC principal");
overviewModel = metricLine(card, "Modelo", "Siemens LOGO! 8 / LOGO! 9");
overviewIp = metricLine(card, "Dirección IP", "192.168.0.3");
overviewModbus = metricLine(card, "Modbus TCP", "Puerto 502");
overviewWeb = metricLine(card, "HMI Web", "Puerto 80");
        return card;
    }

    private View buildConnectionCard() {
        LinearLayout card = card();
        card.addView(section("Conexión"));

        deviceName = field("Nombre del equipo", "PLC principal", InputType.TYPE_CLASS_TEXT);
        card.addView(deviceName);

        ip = field("Dirección IP", "192.168.0.3", InputType.TYPE_CLASS_TEXT);
        card.addView(ip);

        LinearLayout portsRow = new LinearLayout(this);
        portsRow.setOrientation(LinearLayout.HORIZONTAL);
        portsRow.addView(weightedField("Puerto Modbus", "502", 1f, holder -> modbusPort = holder));
        portsRow.addView(spaceH(10));
        portsRow.addView(weightedField("Puerto Web", "80", 1f, holder -> webPort = holder));
        card.addView(portsRow);

        https = new CheckBox(this);
        https.setText("Usar HTTPS para HMI Web");
        https.setTextColor(text);
        https.setButtonTintList(android.content.res.ColorStateList.valueOf(cyan));
        https.setPadding(0, dp(6), 0, 0);
        card.addView(https);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, dp(14), 0, 0);
        Button diagnose = primaryButton("Diagnóstico");
        diagnose.setOnClickListener(v -> diagnose());
        actions.addView(diagnose, new LinearLayout.LayoutParams(0, -2, 1));
        actions.addView(spaceH(10));
        Button webButton = secondaryButton("Abrir HMI Web");
        webButton.setOnClickListener(v -> openWeb());
        actions.addView(webButton, new LinearLayout.LayoutParams(0, -2, 1));
        card.addView(actions);
        Button ioButton = primaryButton("Entradas / Salidas PLC");

ioButton.setOnClickListener(v -> {

    Intent intent =
            new Intent(
                    this,
                    IOActivity.class
            );

    startActivity(intent);

});
card.addView(space(20));
card.addView(ioButton);
        return card;
    }

    private View buildReadCard() {
        LinearLayout card = card();
        card.addView(section("Lectura Modbus"));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(weightedField("Unit ID", "1", 1f, holder -> unitId = holder));
        row.addView(spaceH(10));
        row.addView(weightedField("Dirección", "0", 1f, holder -> address = holder));
        row.addView(spaceH(10));
        row.addView(weightedField("Cantidad", "8", 1f, holder -> quantity = holder));
        card.addView(row);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setPadding(0, dp(14), 0, 0);
        Button registers = primaryButton("Leer HR (FC03)");
        registers.setOnClickListener(v -> readRegisters());
        buttons.addView(registers, new LinearLayout.LayoutParams(0, -2, 1));
        buttons.addView(spaceH(10));
        Button coils = secondaryButton("Leer Coils (FC01)");
        coils.setOnClickListener(v -> readCoils());
        buttons.addView(coils, new LinearLayout.LayoutParams(0, -2, 1));
        card.addView(buttons);

        values = outputBox("Sin lecturas todavía.");
        values.setPadding(dp(14), dp(14), dp(14), dp(14));
        LinearLayout.LayoutParams outputParams = new LinearLayout.LayoutParams(-1, -2);
        outputParams.topMargin = dp(12);
        card.addView(values, outputParams);
        return card;
    }

    private View buildWriteCard() {
        LinearLayout card = card();
        card.addView(section("Escritura controlada"));

        value = field("Valor para escribir", "0", InputType.TYPE_CLASS_NUMBER);
        card.addView(value);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setPadding(0, dp(14), 0, 0);
        Button writeRegister = primaryButton("Escribir registro (FC06)");
        writeRegister.setOnClickListener(v -> confirmWrite(false));
        buttons.addView(writeRegister, new LinearLayout.LayoutParams(0, -2, 1));
        buttons.addView(spaceH(10));
        Button writeCoil = secondaryButton("Conmutar coil (FC05)");
        writeCoil.setOnClickListener(v -> confirmWrite(true));
        buttons.addView(writeCoil, new LinearLayout.LayoutParams(0, -2, 1));
        card.addView(buttons);

        TextView note = body("Seguridad: esta aplicación exige confirmación antes de escribir y no modifica RUN/STOP ni firmware.");
        note.setTextColor(muted);
        note.setPadding(0, dp(12), 0, 0);
        card.addView(note);
        return card;
    }

    private View buildFooterCard() {
        LinearLayout card = card();
        card.addView(section("Observaciones"));
        TextView note = body("Usa esta aplicación dentro de una red confiable. Para acceso remoto se recomienda VPN o gateway seguro en lugar de exponer el PLC directamente a Internet.");
        card.addView(note);
        return card;
    }

    private void diagnose() {
        save();
        setStatus("Comprobando red…", muted, panelAlt);
        connectionSummary.setText("Comprobando puertos y latencia del dispositivo…");
        async(() -> {
            long t1 = System.currentTimeMillis();
            boolean modbusOk = tcp(host(), n(modbusPort, 502));
            long modbusMs = System.currentTimeMillis() - t1;

            long t2 = System.currentTimeMillis();
            boolean webOk = tcp(host(), n(webPort, https.isChecked() ? 443 : 80));
            long webMs = System.currentTimeMillis() - t2;

            ui(() -> {
                if (modbusOk || webOk) {
                    setStatus("PLC CONECTADO", success, successBg);
                } else {
                    setStatus("PLC SIN RESPUESTA", danger, dangerBg);
                }
overviewModbus.setText(
        modbusOk ? "OK - Modbus TCP (" + modbusMs + " ms)" : "Sin respuesta"
);

overviewWeb.setText(
        webOk ? "OK - HMI Web (" + webMs + " ms)" : "Sin respuesta"
);
connectionSummary.setText(
        "Modbus TCP: " + (modbusOk ? "CONECTADO" : "SIN RESPUESTA") +
        "\nHMI Web: " + (webOk ? "CONECTADO" : "SIN RESPUESTA")
                );
                refreshOverview();
            });
        });
    }

    private void readRegisters() {
        save();
        values.setText("Leyendo registros…");
        async(() -> {
            try {
                int base = n(address, 0);
                int[] data = modbus.readHoldingRegisters(host(), n(modbusPort, 502), n(unitId, 1), base, n(quantity, 8), 2200);
                StringBuilder sb = new StringBuilder();
                sb.append("Holding Registers\n\n");
                for (int i = 0; i < data.length; i++) {
                    sb.append("HR ").append(base + i).append(" = ").append(data[i]).append('\n');
                }
                ui(() -> values.setText(sb.toString().trim()));
            } catch (Exception e) {
                ui(() -> values.setText("Error: " + e.getMessage()));
            }
        });
    }

    private void readCoils() {
        save();
        values.setText("Leyendo coils…");
        async(() -> {
            try {
                int base = n(address, 0);
                boolean[] data = modbus.readCoils(host(), n(modbusPort, 502), n(unitId, 1), base, Math.min(n(quantity, 8), 128), 2200);
                StringBuilder sb = new StringBuilder();
                sb.append("Coils\n\n");
                for (int i = 0; i < data.length; i++) {
                    sb.append("Coil ").append(base + i).append(" = ").append(data[i] ? "ON" : "OFF").append('\n');
                }
                ui(() -> values.setText(sb.toString().trim()));
            } catch (Exception e) {
                ui(() -> values.setText("Error: " + e.getMessage()));
            }
        });
    }

    private void confirmWrite(boolean coil) {
        String message;
        if (coil) {
            message = "Se escribirá el estado " + (n(value, 0) != 0 ? "ON" : "OFF") + " en el coil " + n(address, 0) + ".";
        } else {
            message = "Se escribirá el valor " + n(value, 0) + " en el holding register " + n(address, 0) + ".";
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirmar escritura")
                .setMessage(message + "\n\nVerifica el equipo antes de continuar.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Escribir", (d, w) -> doWrite(coil))
                .show();
    }

    private void doWrite(boolean coil) {
        save();
        async(() -> {
            try {
                if (coil) {
                    modbus.writeSingleCoil(host(), n(modbusPort, 502), n(unitId, 1), n(address, 0), n(value, 0) != 0, 2200);
                } else {
                    modbus.writeSingleRegister(host(), n(modbusPort, 502), n(unitId, 1), n(address, 0), n(value, 0), 2200);
                }
                ui(() -> Toast.makeText(this, "Escritura confirmada por el PLC", Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                ui(() -> Toast.makeText(this, "Error de escritura: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void openWeb() {
        save();
        String scheme = https.isChecked() ? "https" : "http";
        String url = scheme + "://" + host() + ":" + n(webPort, https.isChecked() ? 443 : 80) + "/";
        Intent intent = new Intent(this, WebHmiActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void refreshOverview() {
        if (overviewName != null) overviewName.setText(textOf(deviceName, "PLC principal"));
        if (overviewIp != null) overviewIp.setText(hostOrDefault());
        if (overviewModel != null) overviewModel.setText("Base funcional para LOGO! 8 y lista para ampliar a LOGO! 9");
    }

    private void save() {
        SharedPreferences.Editor editor = getSharedPreferences("logo9", MODE_PRIVATE).edit();
        editor.putString("deviceName", textOf(deviceName, "PLC principal"));
        editor.putString("ip", hostOrDefault());
        editor.putString("mp", textOf(modbusPort, "502"));
        editor.putString("wp", textOf(webPort, "80"));
        editor.putBoolean("https", https != null && https.isChecked());
        editor.putString("unit", textOf(unitId, "1"));
        editor.putString("address", textOf(address, "0"));
        editor.putString("quantity", textOf(quantity, "8"));
        editor.putString("value", textOf(value, "0"));
        editor.apply();
        refreshOverview();
    }

    private void load() {
        SharedPreferences prefs = getSharedPreferences("logo9", MODE_PRIVATE);
        setText(deviceName, prefs.getString("deviceName", "PLC principal"));
        setText(ip, prefs.getString("ip", "192.168.0.3"));
        setText(modbusPort, prefs.getString("mp", "502"));
        setText(webPort, prefs.getString("wp", "80"));
        if (https != null) https.setChecked(prefs.getBoolean("https", false));
        setText(unitId, prefs.getString("unit", "1"));
        setText(address, prefs.getString("address", "0"));
        setText(quantity, prefs.getString("quantity", "8"));
        setText(value, prefs.getString("value", "0"));
    }

    private boolean tcp(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1800);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int n(EditText field, int def) {
        try {
            return Integer.parseInt(textOf(field, String.valueOf(def)).trim());
        } catch (Exception e) {
            return def;
        }
    }

    private String host() {
        return textOf(ip, "").trim();
    }

    private String hostOrDefault() {
        String h = host();
        return h.isEmpty() ? "192.168.0.3" : h;
    }

    private void async(Runnable runnable) {
        new Thread(runnable).start();
    }

    private void ui(Runnable runnable) {
        runOnUiThread(runnable);
    }

    private void setStatus(String message, int fg, int bgColor) {
        statusPill.setText(message);
        statusPill.setTextColor(fg);
        statusPill.setBackground(chipBackground(bgColor));
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(cardBackground(panel, Color.parseColor("#183549"), 18, 1));
        return card;
    }

    private TextView title(String textValue, int sizeSp) {
        TextView view = new TextView(this);
        view.setText(textValue);
        view.setTextColor(text);
        view.setTextSize(sizeSp);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        return view;
    }

    private TextView section(String textValue) {
        TextView view = new TextView(this);
        view.setText(textValue);
        view.setTextColor(cyan);
        view.setTextSize(19);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        return view;
    }

    private TextView body(String textValue) {
        TextView view = new TextView(this);
        view.setText(textValue);
        view.setTextColor(text);
        view.setTextSize(14);
        return view;
    }

    private TextView metricLine(LinearLayout parent, String label, String valueText) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(6), 0, dp(6));

        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setTextColor(muted);
        labelView.setTextSize(13);
        labelView.setTypeface(Typeface.DEFAULT_BOLD);

        TextView valueView = new TextView(this);
        valueView.setText(valueText);
        valueView.setTextColor(text);
        valueView.setTextSize(13);
        valueView.setGravity(Gravity.END);

        row.addView(labelView, new LinearLayout.LayoutParams(0, -2, 0.9f));
        row.addView(valueView, new LinearLayout.LayoutParams(0, -2, 1.1f));
        parent.addView(row);
        return valueView;
    }

    private EditText field(String hint, String initial, int inputType) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setText(initial);
        editText.setInputType(inputType);
        styleField(editText);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(10);
        editText.setLayoutParams(params);
        return editText;
    }

    private LinearLayout weightedField(String hint, String initial, float weight, FieldSetter setter) {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setOrientation(LinearLayout.VERTICAL);
        wrap.setLayoutParams(new LinearLayout.LayoutParams(0, -2, weight));

        TextView label = new TextView(this);
        label.setText(hint);
        label.setTextColor(muted);
        label.setTextSize(12);
        wrap.addView(label);

        EditText editText = new EditText(this);
        editText.setText(initial);
        editText.setHint(hint);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        styleField(editText);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(6);
        wrap.addView(editText, params);
        setter.set(editText);
        return wrap;
    }

    private void styleField(EditText editText) {
        editText.setTextColor(text);
        editText.setHintTextColor(muted);
        editText.setSingleLine(true);
        editText.setPadding(dp(14), dp(12), dp(14), dp(12));
        editText.setBackground(cardBackground(panelAlt, Color.parseColor("#1C4258"), 14, 1));
    }

    private TextView outputBox(String content) {
        TextView view = new TextView(this);
        view.setText(content);
        view.setTextColor(text);
        view.setTextSize(13);
        view.setBackground(cardBackground(panelAlt, Color.parseColor("#20465A"), 14, 1));
        return view;
    }

    private Button primaryButton(String textValue) {
        Button button = new Button(this);
        button.setText(textValue);
        button.setAllCaps(false);
        button.setTextColor(bg);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setPadding(dp(14), dp(12), dp(14), dp(12));
        button.setBackground(cardBackground(cyan, cyan, 14, 0));
        return button;
    }

    private Button secondaryButton(String textValue) {
        Button button = new Button(this);
        button.setText(textValue);
        button.setAllCaps(false);
        button.setTextColor(text);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setPadding(dp(14), dp(12), dp(14), dp(12));
        button.setBackground(cardBackground(panelAlt, Color.parseColor("#1E4E68"), 14, 1));
        return button;
    }

    private TextView chip(String textValue, int fg, int bgColor) {
        TextView chip = new TextView(this);
        chip.setText(textValue);
        chip.setTextColor(fg);
        chip.setTextSize(12);
        chip.setTypeface(Typeface.DEFAULT_BOLD);
        chip.setPadding(dp(12), dp(7), dp(12), dp(7));
        chip.setBackground(chipBackground(bgColor));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.rightMargin = dp(8);
        chip.setLayoutParams(params);
        return chip;
    }

    private GradientDrawable chipBackground(int fillColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setCornerRadius(dp(99));
        return drawable;
    }

    private GradientDrawable cardBackground(int fillColor, int strokeColor, int radiusDp, int strokeDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setCornerRadius(dp(radiusDp));
        if (strokeDp > 0) {
            drawable.setStroke(dp(strokeDp), strokeColor);
        }
        return drawable;
    }

    private View space(int dp) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, this.dp(dp)));
        return view;
    }

    private View spaceH(int dp) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(this.dp(dp), 1));
        return view;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private String textOf(EditText editText, String fallback) {
        if (editText == null) return fallback;
        String value = editText.getText() == null ? "" : editText.getText().toString();
        return value.isEmpty() ? fallback : value;
    }

    private void setText(EditText editText, String value) {
        if (editText != null) editText.setText(value);
    }

    private interface FieldSetter {
        void set(EditText holder);
    }
}
