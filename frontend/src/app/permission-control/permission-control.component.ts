import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { User } from '../models/user';
import { PermissionControlService } from './permission-control.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Role } from '../models/role';
import { Permission } from '../models/permission';

declare var bootstrap: any;

@Component({
  selector: 'app-permission-control',
  templateUrl: './permission-control.component.html',
  styleUrl: './permission-control.component.css'
})
export class PermissionControlComponent implements OnInit{

  @ViewChild('modifyUserPermissions') modifyPermissionModal!: ElementRef;
  @ViewChild('modifyUserRole') modifyRoleModal!: ElementRef;

  private modalPermissionInstance: any;
  private modalRoleInstance: any;
  public pageSize: number = 20;
  public currentPage: number = 1;
  public users: User[] = [];
  public roles: Role[] = [];
  public permissions: Permission[] = [];
  public modifyPermissionVisible: boolean = false;
  public modifyRoleVisible: boolean = false;
  public selectedUser: User | null = null;
  public isModerator: boolean = false;

  constructor(private permissionControlService: PermissionControlService) {}

  ngOnInit(): void {
    this.getUsers();
    this.getRoles();
  }

  getUsers() {
    this.permissionControlService.getUsers().subscribe(
      (response: User[]) => {
        this.users = response;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    );
  }

  getRoles() {
    this.permissionControlService.getRoles().subscribe(
      (response: Role[]) => {
        this.roles = response;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

    updateRole(userId: number, role: Role) {
    this.permissionControlService.updateRole(userId, role).subscribe(
      (response: Role) => {
       this.getUsers();
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

   getUsersPermissions(userId: number) {
    this.permissionControlService.getUsersPermissions(userId).subscribe(
      (response: Permission[]) => {
        this.permissions = response;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

  updateUsersPermissions(userId: number, permissions: Permission[]) {
    this.permissionControlService.updateUsersPermissions(userId, permissions).subscribe(
      (response: HttpResponse<any>) => {
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

  showModifyUserRole(user: User) {
    if(!this.modifyRoleVisible) {
      this.modifyRoleVisible = true;
      this.selectedUser = { ...user };

      if(!this.modalRoleInstance)
        this.modalRoleInstance = new bootstrap.Modal(this.modifyRoleModal.nativeElement, {
          backdrop: 'static', // Prevent closing on outside click
          keyboard: false     // Disable closing with Esc key
        });

        this.modalRoleInstance.show();
    }
  }

  async saveUserRole() {
    if(this.selectedUser !== null && this.selectedUser.userId !== null && this.selectedUser.role !== null) {
      await this.updateRole(this.selectedUser.userId, this.selectedUser.role);
      this.closeModifyUserRole();
    }
  }


    closeModifyUserRole() {
      if(this.modifyRoleVisible ) {
        this.modifyRoleVisible = false;
        this.selectedUser = null;
        this.modalRoleInstance.hide();
      }
    }

  async showModifyUserPermissions(user: User) {
    if(!this.modifyPermissionVisible) {
      this.modifyPermissionVisible = true;
      this.selectedUser = { ...user };

      if(this.selectedUser && this.selectedUser.role) {
        if(this.selectedUser.role.roleName === "USER")
          this.isModerator = false;
        else if(this.selectedUser.role.roleName === "MODERATOR")
          this.isModerator = true;
      } 

      if(!this.modalPermissionInstance)
        this.modalPermissionInstance = new bootstrap.Modal(this.modifyPermissionModal.nativeElement, {
          backdrop: 'static', // Prevent closing on outside click
          keyboard: false     // Disable closing with Esc key
        });

        if(user.userId)
            await this.getUsersPermissions(user.userId); 
        this.modalPermissionInstance.show();
    }
  }
    
    saveUserPermissions() {
      if(this.selectedUser !== null && this.selectedUser.userId !== null) {
        this.updateUsersPermissions(this.selectedUser.userId, this.permissions);
        this.closeModifyUserPermissions();
      }
    }
  
  
      closeModifyUserPermissions() {
        if(this.modifyPermissionVisible ) {
          this.modifyPermissionVisible = false;
          this.modalPermissionInstance.hide();
          this.selectedUser = null;
          this.isModerator = false;
          this.permissions = [];
        }
      }

      compareById(idFirst: any, idSecond: any) : boolean { 
        return idFirst && idSecond && idFirst.roleId === idSecond.roleId;
     }

     onPermissionChange(topicName: string, permissionName: string, isChecked: boolean): void {
      var topicPermissions;
        this.permissions.forEach(curr => {
          if(curr.topicName === topicName) { 
            topicPermissions = this.permissions.indexOf(curr);
          }
        })
      
        if(topicPermissions !== undefined) {
        const index = this.permissions[topicPermissions].permissions.indexOf(permissionName);
    
      if (isChecked && index === -1) {
        this.permissions[topicPermissions].permissions.push(permissionName); // Add permission if checked and not present
      } else if (!isChecked && index > -1) {
        this.permissions[topicPermissions].permissions.splice(index, 1); // Remove permission if unchecked and present
      }
    }
    }

      onCheckboxChange(event: Event): boolean {
        return (event.target as HTMLInputElement).checked;
      }
}
