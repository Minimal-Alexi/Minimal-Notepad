# Jenkins
Jenkins automates running tests and generating coverage reports, ensuring code quality and reliability.

## 🔐 Setting Up Credentials in Jenkins
1. Go to: Jenkins Dashboard → Manage Jenkins → Credentials.
![image](https://github.com/user-attachments/assets/29a1064b-0580-4fe6-8ad2-04236c047b07)
3. Select: (global) → Add Credentials.
4. In the New Credentials page:
- Choose Kind: `Secret file`
- Leave Scope default - `Global (Jenkins, nodes, items, all child items, etc`
- File → Upload project's .env file
- Give the ID `minimal-notepad-env-file` and click "Create".
  
![image](https://github.com/user-attachments/assets/b81a8150-0a39-4d7c-9ebd-0a8e519994e5)

## ⚙️ Setting Up Jenkins Pipeline Job
1. Go to: Jenkins Dashboard → New Item.
2. Enter the name `Minimal-Notepad`.
3. Select "Pipeline" and click OK.
4. Scroll to the "Pipeline" section and select:
- Definition: Pipeline script from SCM
- SCM: Git
- Repository URL: https://github.com/Minimal-Alexi/Minimal-Notepad.git
- Branch: */main
- Script Path: Jenkinsfile

![image](https://github.com/user-attachments/assets/86baceea-9df8-4f4b-8544-194fda67ef12)


5. Click "Save" → Click "Build Now"

🎉 Congratulations! You’ve successfully set up your Jenkins pipeline.

This setup will pull the code from the GitHub repository and execute the pipeline defined in the Jenkinsfile in the repository.
