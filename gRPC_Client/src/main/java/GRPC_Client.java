import com.assignment.grpc.User;
import com.assignment.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.logging.Logger;

public class GRPC_Client {
    private static final Logger logger = Logger.getLogger(GRPC_Client.class.getName());

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1111).usePlaintext().build();
        userGrpc.userBlockingStub userBlockingStub = userGrpc.newBlockingStub(managedChannel);
        userGrpc.userStub userStub = userGrpc.newStub(managedChannel);

        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {
            displayMenu();
            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (option) {
                case 1:
                    registerUser(userBlockingStub, scanner);
                    break;
                case 2:
                    login(userBlockingStub, scanner);
                    break;
                case 3:
                    createProfile(userStub, scanner);
                    break;
                case 4:
                    updateProfile(userStub, scanner);
                    break;
                case 5:
                    viewProfile(userStub, scanner);
                    break;
                case 6:
                    // Exit the loop and stop the program
                    managedChannel.shutdown();
                    return;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    public static void displayMenu() {
        System.out.println("ENTER YOUR CHOICE (1-6):");
        System.out.println("1. REGISTER");
        System.out.println("2. LOGIN");
        System.out.println("3. CREATE PROFILE");
        System.out.println("4. UPDATE PROFILE");
        System.out.println("5. VIEW PROFILE");
        System.out.println("6. EXIT");
        System.out.print("ENTER CHOICE >> ");
    }

    private static void registerUser(userGrpc.userBlockingStub userBlockingStub, Scanner scanner) {
        System.out.print("USERNAME : ");
        String username = scanner.nextLine();
        System.out.print("PASSWORD : ");
        String password = scanner.nextLine();

        User.RegistrationRequest request = User.RegistrationRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        User.RegistrationResponse response = userBlockingStub.register(request);
        logger.info("ResposeCode: " + response.getResponseCode());
        logger.info("Message: " + response.getMessage());
    }

    private static void login(userGrpc.userBlockingStub userBlockingStub, Scanner scanner) {
        System.out.print("USERNAME: ");
        String username = scanner.nextLine();
        System.out.print("PASSWORD: ");
        String password = scanner.nextLine();

        User.LoginRequest request = User.LoginRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        User.Response response = userBlockingStub.login(request);
        logger.info("Response Code: " + response.getResponseCode());
        logger.info("Response Message: " + response.getMessage());
    }

    private static void createProfile(userGrpc.userStub userStub, Scanner scanner) {
        System.out.print("USERNAME: ");
        String username = scanner.nextLine();
        System.out.print("FULL NAME: ");
        String fullName = scanner.nextLine();
        System.out.print("EMAIL: ");
        String email = scanner.nextLine();

        User.ProfileRequest request = User.ProfileRequest.newBuilder()
                .setUsername(username)
                .setFullName(fullName)
                .setEmail(email)
                .build();

        StreamObserver<User.ProfileResponse> responseObserver = new StreamObserver<User.ProfileResponse>() {
            @Override
            public void onNext(User.ProfileResponse response) {
                logger.info("Response Code: " + response.getResponseCode());
                logger.info("Response Message: " + response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.severe("Error during profile creation: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("Profile creation completed.");
            }
        };

        userStub.createProfile(request, responseObserver);
    }

    private static void updateProfile(userGrpc.userStub userStub, Scanner scanner) {
        System.out.print("USERNAME: ");
        String username = scanner.nextLine();
        System.out.print("FULL NAME: ");
        String fullName = scanner.nextLine();
        System.out.print("EMAIL: ");
        String email = scanner.nextLine();

        User.UpdateProfileRequest request = User.UpdateProfileRequest.newBuilder()
                .setUsername(username)
                .setFullName(fullName)
                .setEmail(email)
                .build();

        StreamObserver<User.ProfileResponse> responseObserver = new StreamObserver<User.ProfileResponse>() {
            @Override
            public void onNext(User.ProfileResponse response) {
                logger.info("Response Code: " + response.getResponseCode());
                logger.info("Response Message: " + response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.severe("Error during profile update: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("Profile update completed.");
            }
        };

        userStub.updateProfile(request, responseObserver);
    }
    private static void viewProfile(userGrpc.userStub userStub, Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("USERNAME : shawon");
        System.out.println("FULL NAME : shawon majid");
        System.out.println("EMAIL : shawon@gmail.com");
    }
}


