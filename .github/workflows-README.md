# GitHub Workflows Documentation
**Version: January 15, 2025**

This file provides an overview of the automated workflows in this repository. Each workflow enhances project management and streamlines common GitHub actions.

---

### Workflows Overview

1. **Auto Add Estimate Based on Size**
   - Assigns an estimate to issues based on their size field.
   - Maps size labels (e.g., XS, M, L) to estimates if the estimate field is empty.

2. **Auto Add Issue Type**
   - Categorises issues and pull requests by assigning an issue type field based on title keywords (e.g., feature, bug fix, documentation).

3. **Auto Add to Project**
   - Automatically adds newly created or reopened issues and pull requests to a specified GitHub project.

4. **Auto Add Start Date**
   - Sets a start date for issues when their status changes to "In Progress" to help track timelines.

5. **Auto Add Team Label**
   - Applies a "Project Management" label to new issues for team tracking and organisation.

6. **Auto Assign Issue Author**
   - Automatically assigns the issue author to newly created issues.

7. **Auto Assign Pull Request Author**
   - Automatically assigns the pull request author to their own pull requests.

8. **Export Label Configuration**
   - Exports the current repository label configuration for backup or synchronisation.

9. **Sync Labels**
   - Synchronises repository labels with a source repository or configuration file for consistent labelling.

10. **Auto Add End Date**
    - Sets an end date for issues when they are closed, tracking task completion timelines.

---

### Notes
- **File Date**: January 15, 2025  
  Update this document when workflows are modified or new workflows are added.
