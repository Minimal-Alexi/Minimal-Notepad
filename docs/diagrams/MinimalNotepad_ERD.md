# Minimal Notepad ERD
Minimal Notepad is a note-taking application designed so that the users can have an easy time sharing notes together.

![img.png](resources/ERD.png)

## **Entities and Attributes**

### **User**
- **Attributes:**
    - `UserId` (Primary Key)
    - `Username`
    - `Email`
    - `Password`
- **Relationships:**
    - A **User** _writes_ a **Note**.
    - A **User** _owns_ a **Group**.
    - A **User** _is part of_ a **Group**.

### **Note**
- **Attributes:**
    - `NoteId` (Primary Key)
    - `NoteTitle`
    - `NoteText`
    - `NoteColour`
- **Relationships:**
    - A **Note** _is written by_ a **User**.
    - A **Note** _is shared_ with a **Group**.
    - A **Note** _is tagged_ with a **Category**.
    - A **Note** _has_ a **Figure**.

### **Group**
- **Attributes:**
    - `GroupId` (Primary Key)
    - `GroupName`
    - `GroupDescription`
- **Relationships:**
    - A **Group** _is owned by_ a **User**.
    - A **User** _is part of_ a **Group**.
    - A **Note** _is shared_ with a **Group**.

### **Category**
- **Attributes:**
    - `CategoryId` (Primary Key)
    - `CategoryName`
- **Relationships:**
    - A **Category** _is tagged_ to a **Note**.

### **Figure**
- **Attributes:**
    - `FigureId` (Primary Key)
    - `FigureLink`
- **Relationships:**
    - A **Note** _has_ a **Figure**.

---

## **Summary of Relationships**
1. **User ↔ Note** → A user writes multiple notes.
2. **User ↔ Group** → A user can own and be part of multiple groups.
3. **Group ↔ Note** → Notes can be shared within a group.
4. **Note ↔ Category** → A note can be tagged with a category.
5. **Note ↔ Figure** → A note can have associated figures.