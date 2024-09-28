import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class GovernmentSchemeAssistance extends JFrame implements ActionListener {

    private JLabel titleLabel, instructionLabel, responseLabel;
    private JTextField queryField;
    private JButton sendButton, applyButton;
    private HashMap<String, String> schemeDatabase;
    private HashMap<String, String> schemeLinks;

    // Scheme names and links as constants
    private static final String FARMING = "farming";
    private static final String UNEMPLOYMENT = "unemployment";
    private static final String HOUSING = "housing";
    private static final String EDUCATION = "education";
    private static final String HEALTH = "health";
    private static final String BUSINESS = "business";

    private static final String FARMING_LINK = "https://pmkisan.gov.in/";
    private static final String UNEMPLOYMENT_LINK = "https://dge.gov.in/dge/schemes_programmes";
    private static final String HOUSING_LINK = "https://www.india.gov.in/topics/housing";
    private static final String EDUCATION_LINK = "https://www.education.gov.in/scholarships-education-loan-0";
    private static final String HEALTH_LINK = "https://www.india.gov.in/topics/health-family-welfare";
    private static final String BUSINESS_LINK = "https://www.myscheme.gov.in/";

    public GovernmentSchemeAssistance() {
        super("Government Scheme Assistance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 10, 10));

        // Initialize the scheme database
        schemeDatabase = new HashMap<>();
        schemeLinks = new HashMap<>();
        initializeSchemes();

        // UI Components
        titleLabel = new JLabel("Government Scheme Assistance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel);

        instructionLabel = new JLabel("Enter your query (e.g., farming scheme, unemployment scheme):",
                SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(instructionLabel);

        queryField = new JTextField();
        add(queryField);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        add(sendButton);

        responseLabel = new JLabel("Awaiting your query...", SwingConstants.CENTER);
        responseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(responseLabel);

        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);
        add(applyButton);

        setVisible(true);
    }

    // Initialize schemes with their details and links
    private void initializeSchemes() {
        schemeDatabase.put(FARMING, "Details about farming schemes. Some farming schemes: PM-KISAN, PMFBY.");
        schemeLinks.put(FARMING, FARMING_LINK);

        schemeDatabase.put(UNEMPLOYMENT,
                "Details about unemployment schemes. Some unemployment schemes: Atmanirbhar Bharat Rojgar Yojana, PM-SYM.");
        schemeLinks.put(UNEMPLOYMENT, UNEMPLOYMENT_LINK);

        schemeDatabase.put(HOUSING, "Details about housing schemes. Some housing schemes: PMAY, CLSS.");
        schemeLinks.put(HOUSING, HOUSING_LINK);

        schemeDatabase.put(EDUCATION,
                "Details about education schemes. Some education schemes: PM e-VIDYA, Samagra Shiksha.");
        schemeLinks.put(EDUCATION, EDUCATION_LINK);

        schemeDatabase.put(HEALTH, "Details about health schemes. Some health schemes: Ayushman Bharat, PM-JAY.");
        schemeLinks.put(HEALTH, HEALTH_LINK);

        schemeDatabase.put(BUSINESS, "Details about business schemes. Some business schemes: PMEGP, CGTMSE.");
        schemeLinks.put(BUSINESS, BUSINESS_LINK);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String userQuery = queryField.getText().trim();
            if (!userQuery.isEmpty()) {
                String response = getSchemeDetails(userQuery);
                responseLabel.setText("<html>Bot: " + response + "</html>");

                // Add mouse listener for clickable links
                responseLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        JLabel label = (JLabel) evt.getSource();
                        String text = label.getText();
                        String url = extractUrl(text);
                        if (url != null) {
                            try {
                                Desktop.getDesktop().browse(new URI(url));
                            } catch (IOException | URISyntaxException ex) {
                                JOptionPane.showMessageDialog(null, "Error opening link: " + ex.getMessage());
                            }
                        }
                    }
                });
                queryField.setText(""); // Clear the input field
            } else {
                responseLabel.setText("Please enter a query.");
            }
        } else if (e.getSource() == applyButton) {
            showApplyForm(); // Show the popup form for applying
        }
    }

    // Extracts the first href link from the JLabel HTML content
    private String extractUrl(String text) {
        return text.matches(".href=['\"](.?)['\"].") ? text.replaceAll(".*href=['\"](.?)['\"].*", "$1") : null;
    }

    // Get scheme details and generate clickable link
    private String getSchemeDetails(String query) {
        String lowerQuery = query.toLowerCase();
        for (String scheme : schemeDatabase.keySet()) {
            if (lowerQuery.contains(scheme)) {
                return schemeDatabase.get(scheme) + " <br/>Click here to apply: <a href='" + schemeLinks.get(scheme)
                        + "'>Apply now</a>";
            }
        }

        return "Sorry, I couldn't find information on that scheme.";
    }

    // Application form when "Apply" button is clicked
    private void showApplyForm() {
        JFrame applyFrame = new JFrame("Apply for Scheme");
        applyFrame.setSize(300, 300);
        applyFrame.setLocationRelativeTo(null);
        applyFrame.setLayout(new GridLayout(6, 2, 10, 10));
        applyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Form fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        applyFrame.add(nameLabel);
        applyFrame.add(nameField);

        JLabel dobLabel = new JLabel("Date of Birth:");
        JTextField dobField = new JTextField();
        applyFrame.add(dobLabel);
        applyFrame.add(dobField);

        JLabel yearLabel = new JLabel("Passing Year:");
        JTextField yearField = new JTextField();
        applyFrame.add(yearLabel);
        applyFrame.add(yearField);

        JLabel websiteLabel = new JLabel("Website:");
        JLabel websiteLink = new JLabel("<html><a href='https://www.example.com/'>https://www.example.com/</a></html>");
        websiteLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor

        // Add mouse listener for the website link
        websiteLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.example.com"));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, "Error opening link: " + ex.getMessage());
                }
            }
        });

        applyFrame.add(websiteLabel);
        applyFrame.add(websiteLink);

        // Submit button
        JButton submitButton = new JButton("Submit");
        applyFrame.add(new JLabel()); // Empty space
        applyFrame.add(submitButton);

        // Action for the submit button
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String dob = dobField.getText();
            String passingYear = yearField.getText();

            if (!name.isEmpty() && !dob.isEmpty() && !passingYear.isEmpty()) {
                JOptionPane.showMessageDialog(applyFrame, "Form successfully submitted!");
                applyFrame.dispose(); // Close form after submission
            } else {
                JOptionPane.showMessageDialog(applyFrame, "Please fill all fields!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        applyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GovernmentSchemeAssistance::new);
    }
}