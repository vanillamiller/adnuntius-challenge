package com.adnuntius.challenge;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MedicineServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    private String generateEmptyMessage() {
        final JsonObject json = new JsonObject();
        json.addProperty("message", "there are no medications currently stored");
        final Gson gson = new Gson();
        return gson.toJson(json);
    }

    @Test
    public void doGetEmpty() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        new MedicineServlet().doGet(request, response);

        assertEquals(generateEmptyMessage(), stringWriter.toString());

    }

    @Test
    public void doGetNonEmpty() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        new MedicineServlet().doGet(request, response);

        assertEquals(generateEmptyMessage(), stringWriter.toString());

    }

    @Test
    public void doPostCorrectFormat1() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final ArrayList<MedicineString> msList = new ArrayList<MedicineString>();
        msList.add(new MedicineString("186FASc73541_M_1058"));
        msList.add(new MedicineString("18673cda541_S_0061"));
        msList.add(new MedicineString("18673541_S_0146"));
        msList.add(new MedicineString("18673cda541_XL_0056"));
        msList.add(new MedicineString("18896541_M_0055"));
        msList.add(new MedicineString("18896541_XXL_0038"));
        msList.add(new MedicineString("aa1867354cc1_S_0073"));
        
        final String requestBody = "{\"medicationStrings\":\"186FASc73541_M_1058;18673cda541_S_0061;18673541_S_0146;18673cda541_XL_0056;18896541_M_0055;18896541_XXL_0038;aa1867354cc1_S_0073;\"}";
        when(request.getReader()).thenReturn(new BufferedReader( new StringReader(requestBody)));

        when(response.getWriter()).thenReturn(printWriter);
        new MedicineServlet().doPost(request, response);
        assertEquals(new MedicineServlet().generateGetSuccessResponse(msList), stringWriter.toString());
        
    }

    @Test
    public void doPostCorrectFormat2() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final ArrayList<MedicineString> msList = new ArrayList<MedicineString>();
        msList.add(new MedicineString("186FASc73541_M_1058"));
        msList.add(new MedicineString("18673cda541_S_0061"));
        msList.add(new MedicineString("18673541_S_0146"));
        msList.add(new MedicineString("18673cda541_XL_0056"));
        msList.add(new MedicineString("18896541_M_0055"));
        msList.add(new MedicineString("18896541_XXL_0038"));
        msList.add(new MedicineString("aa1867354cc1_S_0073"));
        
        final String requestBody = "{\"medicationStrings\":[\"186FASc73541_M_1058\",\"18673cda541_S_0061\",\"18673541_S_0146\",\"18673cda541_XL_0056\",\"18896541_M_0055\",\"18896541_XXL_0038\",\"aa1867354cc1_S_0073\"]}";
        
        when(request.getReader()).thenReturn(new BufferedReader( new StringReader(requestBody)));
        when(response.getWriter()).thenReturn(printWriter);

        new MedicineServlet().doPost(request, response);
        assertEquals(new MedicineServlet().generateGetSuccessResponse(msList), stringWriter.toString());
        
    }

}