package ovoto.math.unifi.it.server.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import ovoto.math.unifi.it.client.admin.BallotServiceCommunicationErrorException;
import ovoto.math.unifi.it.shared.Ballot;
import ovoto.math.unifi.it.shared.Ballot.Status;

public class BallotUtils {



	public static Ballot setupBallot(Ballot b) throws BallotServiceCommunicationErrorException {

		try {

			URL url = new URL(b.getServiceUrl());


			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(0);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write("mode=SETUP");
			writer.write("&id=" + b.getBallotId());
			writer.write("&accessToken=" + URLEncoder.encode(b.getServiceAccessToken(), "UTF-8"));
			writer.write("&numOfChoices=" + b.getNumOfChoices());
			writer.write("&startDate=" + b.getStartDate().getTime());
			writer.write("&endDate=" + b.getEndDate().getTime());
			writer.write("&ballotText=" + URLEncoder.encode(b.getBallotText(), "UTF-8"));

			int i = 0;
			for(String l : b.getLabels() ) {
				String message = URLEncoder.encode(l, "UTF-8");
				writer.write("&label"+ i++ +"=" + message);
			}
			writer.close();



			String response="";
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// OK
				BufferedReader reader =	new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					response += line;
				}
				reader.close();
				b.setServiceAccessId(response);
			} else {
				throw new BallotServiceCommunicationErrorException("Communication Error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
		} catch (Exception e) {
			throw new BallotServiceCommunicationErrorException(e.getMessage());
		}

		System.err.println("GOT:" + b.getServiceAccessId());
		
		
		b.setStatus(Status.READY);
		
		writeBallot(b);
		return b;

	}

	public static Long writeBallot(Ballot b) {
		//TODO Verify that the input is valid. 
		Objectify ofy = ObjectifyService.begin();
		ofy.put(b);
		return b.getBallotId();
	}




}
